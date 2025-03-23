package arsw.tamaltolimense.bidservice.service.bidservice.impl;

import arsw.tamaltolimense.bidservice.classes.Bid;
import arsw.tamaltolimense.bidservice.exception.BidException;
import arsw.tamaltolimense.bidservice.service.bidservice.BidService;

import org.springframework.stereotype.Service;

@Service
public class BidServiceImpl implements BidService {


    @Override
    public Bid startBet(String containerId, int initialValue) throws BidException {
        if(containerId == null || containerId.trim().isEmpty()) throw new BidException(BidException.NULL_VALUE);
        if(initialValue < 0) throw new BidException(BidException.NEGATIVE_VALUE);
        return new Bid(containerId,initialValue);
    }


    @Override
    public Bid offer(int amount, int limit, Bid bid,String newOwner) throws BidException{
        if(newOwner == null || newOwner.trim().isEmpty()) throw new BidException(BidException.NULL_OWNER);
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

    /**
     * Indicates if a string is null or empty
     * @param value string that is going to be analyzed
     * @return if the string is null or empty
     */
    private boolean isNull(String value){
        return value == null || value.trim().isEmpty();
    }



}