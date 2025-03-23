package arsw.tamaltolimense.bidservice.controller;

import arsw.tamaltolimense.bidservice.classes.Bid;
import arsw.tamaltolimense.bidservice.exception.BidException;
import arsw.tamaltolimense.bidservice.service.bidservice.BidService;
import arsw.tamaltolimense.bidservice.service.notificationservice.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

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


    @MessageMapping("/single/{container}")
    public void makeOffer(@DestinationVariable String container, @Payload Map<String, Object> payload) {
        String newOwner = (String) payload.get("newOwner");
        try{
            int amount = (int) payload.get("amount");
            int limit = (int) payload.get("limit");
            Bid bid = (Bid) payload.get("bid");

            messagingTemplate.convertAndSend(WebSocketBid.SIMPLE_BLOKER + container,
                    bidService.offer(amount,limit,bid,newOwner));
        }catch (BidException e) {
            NotificationService.sendMessage(newOwner, "Error en in the offer2: " + e.getMessage());
        }catch (ClassCastException e) {
            NotificationService.sendMessage((String) payload.get("newOwner"),
                    "Error in the request: " + e.getMessage());
        }
    }

    @MessageMapping("/pair/{container}")
    public void makePairOffer(@DestinationVariable String container, @Payload Map<String, Object> payload) {
        String newOwner1 = (String) payload.get("newOwner1");
        String newOwner2 = (String) payload.get("newOwner2");
        try{
            int amount = (int) payload.get("amount");
            int limit1 = (int) payload.get("limit1");
            int limit2 = (int) payload.get("limit1");
            Bid bid = (Bid) payload.get("bid");
            messagingTemplate.convertAndSend(WebSocketBid.SIMPLE_BLOKER + container,
                    bidService.offerInPairs(amount,limit1,limit2,bid,newOwner1,newOwner2));
        }catch (BidException e) {
            String nickname = newOwner1;
            NotificationService.sendMessage(nickname, "Error en in the offer: " + e.getMessage());
            nickname = newOwner2;
            NotificationService.sendMessage(nickname, "Error en in the offer: " + e.getMessage());
        }catch (ClassCastException e) {
            NotificationService.sendMessage((String) payload.get("newOwner2"),
                    "Error in the request: " + e.getMessage());
        }
    }



}
