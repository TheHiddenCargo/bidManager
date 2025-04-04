package arsw.tamaltolimense.bidservice.service.impl;

import arsw.tamaltolimense.bidservice.classes.Bid;
import arsw.tamaltolimense.bidservice.exception.BidException;
import arsw.tamaltolimense.bidservice.service.BidService;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class BidServiceImpl implements BidService {


    @Override
    public Bid startBet(String containerId, int initialValue, int realValue) throws BidException {
        if(containerId == null || containerId.trim().isEmpty()) throw new BidException(BidException.NULL_VALUE);
        if(initialValue < 0) throw new BidException(BidException.NEGATIVE_VALUE);
        if(realValue < 0) throw new BidException(BidException.NEGATIVE_VALUE);
        if(realValue <= initialValue) throw new BidException(BidException.GREATER_VALUE);
        return new Bid(containerId,initialValue,realValue);
    }


    @Override
    public Bid offer(int amount, int limit, Bid bid,String newOwner) throws BidException{
        if(isNull(newOwner)) throw new BidException(BidException.NULL_OWNER);
        if(bid.getAmountOffered() + amount > limit) throw new BidException(BidException.EXCEED_LIMIT);
        bid.bet(newOwner,null,amount);
        return bid;
    }

    @Override
    public Bid offerInPairs(int amount, int limit1, int limit2, Bid bid, String newOwner1, String newOwner2) throws BidException {
        if(isNull(newOwner1) || isNull(newOwner2)) throw new BidException(BidException.NULL_OWNERS);
        if(bid.getAmountOffered() + amount > limit1 + limit2) throw new BidException(BidException.EXCEED_LIMIT);
        float split = (float) (amount + bid.getAmountOffered()) /2;
        if(split > limit1 || split > limit2) throw new BidException(BidException.EXCEED_OWNER);
        bid.bet(newOwner1,newOwner2,amount);
        return bid;
    }

    @Override
    public int calculate(Bid bid){
        bid.close();
        return bid.calculate();
    }



    /**
     * Indicates if a string is null or empty
     * @param value string that is going to be analyzed
     * @return if the string is null or empty
     */
    private boolean isNull(String value){
        return value == null || value.trim().isEmpty();
    }

    @Override
    public Bid convertToBid(Map<String,Object> data){
        Bid bid;
        try{
            bid = (Bid) data;
        }catch (ClassCastException e){

            String containerId = (String) data.get("containerId");
            int amountOffered = (int) (data.get("amountOffered"));
            int realValue = (int) (data.get("realValue"));

            bid = new Bid(containerId, amountOffered, realValue);

            if(data.keySet().contains("owner1")) bid.setOwner1((String) data.get("owner1"));
            if(data.keySet().contains("owner2")) bid.setOwner2((String) data.get("owner2"));

            boolean isOpen = (boolean) data.get("open");

            if(!isOpen) bid.close();

        }

        return bid;

    }



}