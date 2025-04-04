package arsw.tamaltolimense.bidservice.service;

import arsw.tamaltolimense.bidservice.classes.Bid;
import arsw.tamaltolimense.bidservice.exception.BidException;

import java.util.Map;

public interface BidService {

    /**
     * Opens a bid
     * @param containerId, container's id that is bet on
     * @param initialValue, initial value of the bid
     * @param realValue, real value of the container
     * @return Bid result
     * @throws BidException if container's id is null or initial value negative
     */
    Bid startBet(String containerId, int initialValue, int realValue)throws BidException;


    /**
     * Offer a new amount
     * @param amount, an offer to win the bet
     * @param limit, maximum value of the bet
     * @param bid, bid that is o game
     * @param newOwner, person who makes the bet
     * @return bid result
     * @throws BidException if the next value of the bid exceeds the limit or if the owner is null
     */
    Bid offer(int amount, int limit, Bid bid, String newOwner) throws BidException;

    /**
     * Two people offer a new amount
     * @param amount, an offer to win the bet
     * @param limit1, maximum individual value of the bet for the first person
     * @param limit2, maximum individual value of the bet for the first person
     * @param bid, bid that is o game
     * @param newOwner1, name of the first owner
     * @param newOwner2, name of the second owner
     * @return bid result
     * @throws BidException if both owners are null or empty, or if the amount offered exceeds how much they can
     pay together or if the half of the offered amount exceeds how much can pay each
     */
    Bid offerInPairs(int amount, int limit1, int limit2, Bid bid, String newOwner1, String newOwner2) throws BidException;

    /**
     * Calculates if the owner wins or lose
     * @param bid, current bid
     * @return money that is going to win or lose the owners.
     */
    int calculate (Bid bid);


    Bid convertToBid(Map<String,Object> data);




}
