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

    @MessageMapping("/single/offer")
    public void makeOffer(@Payload int amount,@Payload int limit, @Payload Bid bid,@Payload String newOwner) {
        try{
            int newValue = bidService.offer(amount,limit,bid,newOwner);
            messagingTemplate.convertAndSend(WebSocketBid.SIMPLE_BLOKER + bidService.getContainerId(bid), newValue);
        }catch (BidException e) {
            messagingTemplate.convertAndSend(WebSocketBid.SIMPLE_BLOKER + bidService.getContainerId(bid),
                    bidService.getBet(bid));
        }
    }

    @MessageMapping("/pair/offer")
    public void makePairOffer(@Payload int amount,@Payload int limit1, @Payload int limit2,@Payload Bid bid
            ,@Payload String newOwner1,@Payload String newOwner2) {
        try{
            int newValue = bidService.offerInPairs(amount,limit1,limit2,bid,newOwner1,newOwner2);
            messagingTemplate.convertAndSend(WebSocketBid.SIMPLE_BLOKER + bidService.getContainerId(bid), newValue);
        }catch (BidException e) {
            messagingTemplate.convertAndSend(WebSocketBid.SIMPLE_BLOKER + bidService.getContainerId(bid),
                    bidService.getBet(bid));
        }
    }


}
