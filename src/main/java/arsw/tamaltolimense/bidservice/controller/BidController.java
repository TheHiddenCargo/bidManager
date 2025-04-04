package arsw.tamaltolimense.bidservice.controller;

import arsw.tamaltolimense.bidservice.classes.Bid;
import arsw.tamaltolimense.bidservice.exception.BidException;
import arsw.tamaltolimense.bidservice.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("bids/")
public class BidController {

    private final BidService bidService;

    @Autowired
    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    /**
     * Start a new bid for a container
     * @param requestParams map containing container ID, initialValue, and realValue
     * @return the created bid
     */
    @PostMapping("/start")
    public ResponseEntity<Object> createBid(@RequestParam Map<String, String> requestParams) {
        try {
            String container = requestParams.get("idContainer");
            int initialValue = Integer.parseInt(requestParams.get("initialValue"));
            // Default realValue to a higher value if not provided (to be overridden in frontend)
            int realValue = requestParams.containsKey("realValue")
                    ? Integer.parseInt(requestParams.get("realValue"))
                    : initialValue * 2; // Default placeholder for real value

            return new ResponseEntity<>(bidService.startBet(container, initialValue, realValue), HttpStatus.CREATED);
        } catch (BidException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid number format: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Unexpected error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Place a bid offer on a container
     * @param requestParams map containing container ID, owner, and amount
     * @return the updated bid
     */
    @PostMapping("/offer")
    public ResponseEntity<Object> placeBidOffer(@RequestParam Map<String, String> requestParams) {
        try {
            String containerId = requestParams.get("container");
            String owner = requestParams.get("owner");
            int amount = Integer.parseInt(requestParams.get("amount"));

            // Optional second owner
            String owner2 = requestParams.getOrDefault("owner2", null);

            Bid updatedBid = bidService.placeBid(containerId, owner, owner2, amount);
            return new ResponseEntity<>(updatedBid, HttpStatus.OK);
        } catch (BidException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Invalid number format: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Unexpected error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get current state of a bid for a container
     * @param containerId the container ID
     * @return the current bid
     */
    @GetMapping("/{containerId}")
    public ResponseEntity<Object> getBid(@PathVariable String containerId) {
        try {
            Bid bid = bidService.getBidByContainer(containerId);
            if (bid == null) {
                return new ResponseEntity<>("No bid found for container: " + containerId, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(bid, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error retrieving bid: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Calculate the profit/loss for a bid
     * @param containerId the container ID
     * @return the calculation result
     */
    @GetMapping("/calculate/{containerId}")
    public ResponseEntity<Object> calculateBid(@PathVariable String containerId) {
        try {
            Bid bid = bidService.getBidByContainer(containerId);
            if (bid == null) {
                return new ResponseEntity<>("No bid found for container: " + containerId, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(bidService.calculate(bid), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error calculating bid: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Close a bid for a container
     * @param containerId the container ID
     * @return the closed bid
     */
    @PostMapping("/close/{containerId}")
    public ResponseEntity<Object> closeBid(@PathVariable String containerId) {
        try {
            Bid bid = bidService.closeBid(containerId);
            return new ResponseEntity<>(bid, HttpStatus.OK);
        } catch (BidException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error closing bid: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}