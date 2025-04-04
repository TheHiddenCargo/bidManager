package arsw.tamaltolimense.bidservice.classes;

import arsw.tamaltolimense.bidservice.exception.BidException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class Bid {

    private final String containerId;
    @Setter private int amountOffered;
    private final int realValue;
    @Setter private String owner1;
    @Setter private String owner2;
    private boolean isOpen;
    private LocalDateTime lastBidTime;

    /**
     * Bid's constructor
     * @param containerId, container's id being bet on
     * @param initialValue, initial value of the container
     * @param realValue, real value of the container
     */
    public Bid(String containerId, int initialValue, int realValue) {
        this.containerId = containerId;
        this.amountOffered = initialValue;
        this.realValue = realValue;
        this.isOpen = true;
        this.lastBidTime = LocalDateTime.now();
    }

    /**
     * Place a new bid with a single owner
     * @param owner new owner of the bid
     * @param amount amount to increase the bid by
     * @throws BidException if bid is closed or amount is invalid
     */
    public void placeBid(String owner, int amount) throws BidException {
        if (!isOpen) throw new BidException(BidException.CLOSE);
        if (amount <= 0) throw new BidException(BidException.ZERO_AMOUNT);
        if (amount <= amountOffered) throw new BidException("New bid must be higher than current amount");

        this.amountOffered = amount; // Set the new amount directly (not adding to existing)
        this.owner1 = owner;
        this.owner2 = null; // Clear second owner when a new bid is placed
        this.lastBidTime = LocalDateTime.now();
    }

    /**
     * Place a new bid with two owners
     * @param owner1 primary owner of the bid
     * @param owner2 secondary owner of the bid
     * @param amount the new bid amount (not incremental)
     * @throws BidException if bid is closed or amount is invalid
     */
    public void placeBid(String owner1, String owner2, int amount) throws BidException {
        if (!isOpen) throw new BidException(BidException.CLOSE);
        if (amount <= 0) throw new BidException(BidException.ZERO_AMOUNT);
        if (amount <= amountOffered) throw new BidException("New bid must be higher than current amount");

        this.amountOffered = amount; // Set the new amount directly
        this.owner1 = owner1;
        this.owner2 = owner2;
        this.lastBidTime = LocalDateTime.now();
    }

    /**
     * Legacy method to maintain compatibility
     * @param owner1 primary owner of the bid
     * @param owner2 secondary owner of the bid
     * @param amount amount to increase the bid by
     * @throws BidException if the amount offered is 0 or less
     */
    public void bet(String owner1, String owner2, int amount) throws BidException {
        placeBid(owner1, owner2, this.amountOffered + amount);
    }

    /**
     * Calculate the profit (or loss) based on real value versus bid amount
     * @return difference between real value and amount offered
     */
    public int calculate() {
        return realValue - amountOffered;
    }

    /**
     * Get the current owner of the bid
     * @return the primary owner of the bid
     */
    public String getCurrentOwner() {
        return owner1;
    }

    /**
     * Check if a bid has a secondary owner
     * @return true if there is a valid secondary owner
     */
    public boolean hasSecondaryOwner() {
        return owner2 != null && !owner2.isEmpty();
    }

    /**
     * Close the bid
     */
    public void close() {
        this.isOpen = false;
    }

    /**
     * Get time elapsed since last bid in seconds
     * @return seconds since last bid
     */
    public long getSecondsSinceLastBid() {
        return java.time.Duration.between(lastBidTime, LocalDateTime.now()).getSeconds();
    }

    /**
     * Check if this bid is for the specified container
     * @param containerId the container ID to check
     * @return true if this bid is for the specified container
     */
    public boolean isForContainer(String containerId) {
        return this.containerId.equals(containerId);
    }
}