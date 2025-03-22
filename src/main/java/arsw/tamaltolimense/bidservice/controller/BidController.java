package arsw.tamaltolimense.bidservice.controller;


import arsw.tamaltolimense.bidservice.classes.Bid;
import arsw.tamaltolimense.bidservice.exception.BidException;
import arsw.tamaltolimense.bidservice.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bids/")
public class BidController {

    private BidService bidService;

    @Autowired
    public BidController(BidService bidService) {this.bidService = bidService;}

    @PostMapping("/start/{nickName}")
    public ResponseEntity<Object> getUserInfo(@PathVariable("nickName") String nickName) {
        try{
            return new ResponseEntity<>(bidService.startBet(nickName),HttpStatus.CREATED);

        }catch (BidException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/offer/single")
    public ResponseEntity<Object> singleOffer(@RequestParam("amount") int amount,
                                              @RequestParam("limit") int limit, @RequestBody Bid bid,
                                              @RequestParam("newOwner") String newOwner) {
        try{
            return new ResponseEntity<>(bidService.offer(amount,limit,bid,newOwner),HttpStatus.CREATED);

        }catch (BidException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/offer/pairs")
    public ResponseEntity<Object> pairOffer(@RequestParam("amount") int amount,
                                              @RequestParam("limit1") int limit1, @RequestParam("limit2") int limit2,
                                            @RequestBody Bid bid,
                                              @RequestParam("newOwner1") String newOwner1,
                                            @RequestParam("newOwner2") String newOwner2) {
        try{
            return new ResponseEntity<>(bidService.offerInPairs(amount,limit1,limit2,bid,newOwner1,newOwner2),
                    HttpStatus.CREATED);

        }catch (BidException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


}
