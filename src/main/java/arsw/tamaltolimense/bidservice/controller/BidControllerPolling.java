package arsw.tamaltolimense.bidservice.controller;

import arsw.tamaltolimense.bidservice.classes.Bid;
import arsw.tamaltolimense.bidservice.exception.BidException;
import arsw.tamaltolimense.bidservice.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;


import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@RestController
@RequestMapping("polling/bids")
@CrossOrigin(origins = "*")
public class BidControllerPolling {

    private final BidService bidService;

    private static final String BID = "bid";

    private static final String ERROR = "error";


    private final ConcurrentMap<String, Queue<Bid>> bidMessages;

    @Value("${app.polling.timeout}")
    private long pollingTimeout;

    @Value("${app.polling.check-interval}")
    private long checkInterval;

    @Value("${app.polling.max-queue-size:100}")
    private int maxQueueSize;



    @Autowired
    public BidControllerPolling(BidService bidService) {
        this.bidService = bidService;
        bidMessages = new ConcurrentHashMap<>();
    }

    @PostMapping("/publish/{containerId}")
    public ResponseEntity<Object> publishBid(@PathVariable String containerId, @RequestBody Bid bidData) {

        Queue<Bid> queueBids = bidMessages.computeIfAbsent(containerId, k -> new ConcurrentLinkedQueue<>());
        if (queueBids.size() >= maxQueueSize) {
            queueBids.poll();
        }
        queueBids.add(bidData);

        return ResponseEntity.ok().body(Map.of("status", "published"));
    }

    @GetMapping("/subscribe/{containerId}")
    public ResponseEntity<Object> subscribeToBids(@PathVariable String containerId) {
        Queue<Bid> queueBids = bidMessages.computeIfAbsent(containerId, k -> new ConcurrentLinkedQueue<>());

        long startTime = System.currentTimeMillis();


        while (System.currentTimeMillis() - startTime < pollingTimeout) {
            Bid bid = queueBids.poll();

            if (bid != null) {
                // Si hay un mensaje, devolverlo inmediatamente
                return ResponseEntity.ok().body(bid);
            }

            try {

                Thread.sleep(checkInterval);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return ResponseEntity.status(503).body(Map.of(ERROR, "Service interrupted"));
            }
        }

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/offer")
    public ResponseEntity<Object> makeBidOffer(@RequestBody Map<String, Object> offerData) {
        try {
            Bid bid = bidService.convertToBid((Map<String, Object>) offerData.get(BID));
            String containerId = bid.getContainerId();
            int amount = (int) offerData.get("amount");
            int limit = (int) offerData.get("limit");
            String newOwner = (String) offerData.get("owner");

            bidService.offer(amount, limit, bid, newOwner);

            this.publishBid(containerId, bid);

            return ResponseEntity.ok(bid);
        } catch (BidException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(ERROR, e.getMessage()));
        }
    }

    @PostMapping("/pair-offer")
    public ResponseEntity<Object> makePairBidOffer(@RequestBody Map<String, Object> offerData) {
        try {
            Bid bid = bidService.convertToBid((Map<String, Object>)offerData.get(BID));
            String containerId = bid.getContainerId();
            int amount = (int) offerData.get("amount");
            int limit1 = (int) offerData.get("limit1");
            int limit2 = (int) offerData.get("limit2");
            String newOwner1 = (String) offerData.get("owner1");
            String newOwner2 = (String) offerData.get("owner2");


            bidService.offerInPairs(amount, limit1, limit2, bid, newOwner1, newOwner2);

            this.publishBid(containerId, bid);

            return ResponseEntity.ok(bid);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of(ERROR, e.getMessage()));
        }
    }

    @Scheduled(fixedRateString = "${app.polling.cleanup-interval:300000}")
    public void cleanupEmptyQueues() {
        bidMessages.entrySet().removeIf(entry -> entry.getValue().isEmpty());
    }
}
