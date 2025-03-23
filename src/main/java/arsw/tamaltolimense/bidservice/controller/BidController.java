package arsw.tamaltolimense.bidservice.controller;


import arsw.tamaltolimense.bidservice.exception.BidException;
import arsw.tamaltolimense.bidservice.service.bidservice.BidService;
import arsw.tamaltolimense.bidservice.service.notificationservice.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("bids/")
public class BidController {

    private BidService bidService;

    private NotificationService notificationService;

    @Autowired
    public BidController(BidService bidService, NotificationService notificationService) {
        this.bidService = bidService;
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications/{nickName}")
    public void getNotifications(@PathVariable String nickName) {
        notificationService.addEmitter(nickName, new SseEmitter());
    }

    @PostMapping("/start")
    public ResponseEntity<Object> getUserInfo(@RequestParam("idContainer") String container
            ,@RequestParam("initialValue") int initialValue) {
        try{
            return new ResponseEntity<>(bidService.startBet(container,initialValue),HttpStatus.CREATED);
        }catch (BidException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
