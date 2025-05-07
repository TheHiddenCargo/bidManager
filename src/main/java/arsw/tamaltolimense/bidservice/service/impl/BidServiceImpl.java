package arsw.tamaltolimense.bidservice.service.impl;

import arsw.tamaltolimense.bidservice.classes.Bid;
import arsw.tamaltolimense.bidservice.exception.BidException;
import arsw.tamaltolimense.bidservice.service.BidService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BidServiceImpl implements BidService {

    // In-memory storage for active bids
    private final Map<String, Bid> activeBids = new ConcurrentHashMap<>();

    /**
     * Start a new bid for a container
     * @param containerId the container ID
     * @param initialValue initial bid value
     * @param realValue real value of the container
     * @return the created Bid
     * @throws BidException if a bid already exists for this container
     */
    @Override
    public Bid startBet(String containerId, int initialValue, int realValue) throws BidException {
        if(containerId == null || containerId.trim().isEmpty()) throw new BidException(BidException.NULL_VALUE);
        if(initialValue < 0 || realValue < 0) throw new BidException(BidException.NEGATIVE_VALUE);
        if(realValue < initialValue) throw new BidException(BidException.GREATER_VALUE);
        if (activeBids.containsKey(containerId)) throw new BidException(BidException.BID_EXISTS + containerId);

        Bid newBid = new Bid(containerId, initialValue, realValue);
        activeBids.put(containerId, newBid);
        return newBid;
    }

    /**
     * Place a bid on a containe
     * @param containerId the container ID
     * @param owner1 the primary owner
     * @param owner2 the secondary owner (can be null)
     * @param amount the bid amount
     * @return the updated Bid
     * @throws BidException if the bid is invalid or container not found
     */
    @Override
    public Bid placeBid(String containerId, String owner1, String owner2, int amount) throws BidException {
        Bid bid = activeBids.get(containerId);
        if (bid == null) throw new BidException(BidException.BID_NOT_EXISTS + containerId);


        if (!bid.isOpen()) throw new BidException(BidException.CLOSE);

        if(owner1 == null || owner1.trim().isEmpty()) {
            throw new BidException(BidException.NULL_OWNER);
        }

        if (owner2 != null && !owner2.isEmpty()) {

            bid.placeBid(owner1, owner2, amount);
        } else {
            bid.placeBid(owner1, amount);
        }

        return bid;
    }

    /**
     * Get a bid by container ID
     * @param containerId the container ID
     * @return the Bid or null if not found
     */
    @Override
    public Bid getBidByContainer(String containerId) {
        return activeBids.get(containerId);
    }

    /**
     * Calculate the profit/loss for a bid
     * @param bid the Bid to calculate
     * @return the calculation result (profit/loss)
     */
    @Override
    public int calculate(Bid bid) {
        return bid.calculate();
    }

    /**
     * Close a bid for a container
     * @param containerId the container ID
     * @return the closed Bid
     * @throws BidException if the container is not found
     */
    @Override
    public Bid closeBid(String containerId) throws BidException {
        Bid bid = activeBids.get(containerId);
        if (bid == null) {
            throw new BidException("No active bid found for container: " + containerId);
        }

        bid.close();
        return bid;
    }

    /**
     * Get all active bids
     * @return map of all active bids
     */
    @Override
    public Map<String, Bid> getAllActiveBids() {
        return activeBids;
    }
}