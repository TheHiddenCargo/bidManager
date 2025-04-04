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

    private BidService bidService;



    @Autowired
    public BidController(BidService bidService) {
        this.bidService = bidService;
    }


    @PostMapping("/start")
    public ResponseEntity<Object> createBid(@RequestBody Map<String,String> bidData) {
        try{
            String container = bidData.get("container");
            int initialValue = Integer.parseInt(bidData.get("initialValue"));
            int realValue = Integer.parseInt(bidData.get("realValue"));
            return new ResponseEntity<>(bidService.startBet(container,initialValue,realValue),HttpStatus.CREATED);
        }catch (BidException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/calculate")
    public ResponseEntity<Object> calculateBid(@RequestBody Bid bid) {
        return new ResponseEntity<>(bidService.calculate(bid),HttpStatus.OK);
    }

}
