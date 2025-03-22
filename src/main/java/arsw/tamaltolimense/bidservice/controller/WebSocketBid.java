package arsw.tamaltolimense.bidservice.controller;

import arsw.tamaltolimense.bidservice.classes.Bid;
import arsw.tamaltolimense.bidservice.exception.BidException;
import arsw.tamaltolimense.bidservice.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketBid {


    private BidService bidService;
    private SimpMessagingTemplate messagingTemplate;
    private static final String SIMPLE_BLOKER = "/offered/";

    @Autowired
    public WebSocketBid(BidService bidService, SimpMessagingTemplate messagingTemplate) {
        this.bidService = bidService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/start")
    public void startBet(@Payload String bid) {
        try{
            messagingTemplate.convertAndSend(WebSocketBid.SIMPLE_BLOKER + bid,
                    bidService.startBet(bid));
        }catch (BidException e) {
            e.printStackTrace();
        }
    }

    @MessageMapping("/single/offer")
    public void makeOffer(@Payload int amount,@Payload int limit, @Payload Bid bid,@Payload String newOwner) {
        try{
            messagingTemplate.convertAndSend(WebSocketBid.SIMPLE_BLOKER + bidService.getContainerId(bid),
                    bidService.offer(amount,limit,bid,newOwner));
        }catch (BidException e) {
            messagingTemplate.convertAndSend(WebSocketBid.SIMPLE_BLOKER + bidService.getContainerId(bid),
                    bid);
        }
    }

    @MessageMapping("/pair/offer")
    public void makePairOffer(@Payload int amount,@Payload int limit1, @Payload int limit2,@Payload Bid bid
            ,@Payload String newOwner1,@Payload String newOwner2) {
        try{
            messagingTemplate.convertAndSend(WebSocketBid.SIMPLE_BLOKER + bidService.getContainerId(bid),
                    bidService.offerInPairs(amount,limit1,limit2,bid,newOwner1,newOwner2));
        }catch (BidException e) {
            messagingTemplate.convertAndSend(WebSocketBid.SIMPLE_BLOKER + bidService.getContainerId(bid),
                    bid);
        }
    }


}
