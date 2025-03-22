package arsw.tamaltolimense.bidservice.service;

import arsw.tamaltolimense.bidservice.classes.Bid;
import arsw.tamaltolimense.bidservice.exception.BidException;

public interface BidService {

    Bid startBet(String containerId)throws BidException;

    String getContainerId(Bid bid);

    int getBet(Bid bid);

    int offer(int amount, int limit, Bid bid, String newOwner) throws BidException;

    int offerInPairs(int amount, int limit1, int limit2, Bid bid, String newOwner1, String newOwner2) throws BidException;





}
