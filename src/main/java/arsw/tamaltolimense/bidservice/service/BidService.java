package arsw.tamaltolimense.bidservice.service;

import arsw.tamaltolimense.bidservice.classes.Bid;
import arsw.tamaltolimense.bidservice.exception.BidException;

import java.util.Map;

public interface BidService {

    /**
     * Start a new bid for a container
     * @param containerId the container ID
     * @param initialValue initial bid value
     * @param realValue real value of the container
     * @return the created Bid
     * @throws BidException if a bid already exists for this container
     */
    Bid startBet(String containerId, int initialValue, int realValue) throws BidException;

    /**
     * Place a bid on a container
     * @param containerId the container ID
     * @param owner1 the primary owner
     * @param owner2 the secondary owner (can be null)
     * @param amount the bid amount
     * @return the updated Bid
     * @throws BidException if the bid is invalid or container not found
     */
    Bid placeBid(String containerId, String owner1, String owner2, int amount) throws BidException;

    /**
     * Get a bid by container ID
     * @param containerId the container ID
     * @return the Bid or null if not found
     */
    Bid getBidByContainer(String containerId);

    /**
     * Calculate the profit/loss for a bid
     * @param bid the Bid to calculate
     * @return the calculation result (profit/loss)
     */
    int calculate(Bid bid);

    /**
     * Close a bid for a container
     * @param containerId the container ID
     * @return the closed Bid
     * @throws BidException if the container is not found
     */
    Bid closeBid(String containerId) throws BidException;

    /**
     * Get all active bids
     * @return map of all active bids
     */
    Map<String, Bid> getAllActiveBids();
}