package arsw.tamaltolimense.bidservice.service.impl;

import arsw.tamaltolimense.bidservice.classes.Bid;
import arsw.tamaltolimense.bidservice.exception.BidException;
import arsw.tamaltolimense.bidservice.service.BidService;

import org.springframework.stereotype.Service;

@Service
public class BidServiceImpl implements BidService {

    @Override
    public Bid startBet(String containerId) throws BidException {
        if(containerId == null || containerId.trim().isEmpty()) throw new BidException(BidException.NULL_VALUE);
        return new Bid(containerId);
    }

    @Override
    public String getContainerId(Bid bid) {
        return bid.getContainerId();
    }

    @Override
    public int getBet(Bid bid) {
        return bid.getAmountOffered();
    }

    @Override
    public Bid offer(int amount, int limit, Bid bid,String newOwner) throws BidException{
        if(bid.getAmountOffered() + amount > limit) throw new BidException(BidException.EXCEED_LIMIT);
        if(amount <= 0) throw new BidException(BidException.ZERO_AMOUNT);
        bid.bet(newOwner,null,amount);
        return bid;
    }

    @Override
    public Bid offerInPairs(int amount, int limit1, int limit2, Bid bid, String newOwner1, String newOwner2) throws BidException {
        if(bid.getAmountOffered() + amount > limit1 + limit2) throw new BidException(BidException.EXCEED_LIMIT);
        int split = Math.round((float) (amount + bid.getAmountOffered())/2);
        if(split > limit1 || split > limit2) throw new BidException(BidException.EXCEED_LIMIT);
        if(amount <= 0) throw new BidException(BidException.ZERO_AMOUNT);
        bid.bet(newOwner1,newOwner2,amount);
        return bid;
    }



}
