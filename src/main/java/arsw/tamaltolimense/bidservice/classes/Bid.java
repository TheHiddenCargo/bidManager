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

        if (amount <= 0) throw new BidException(BidException.ZERO_AMOUNT);
        if (amount <= amountOffered) throw new BidException("New bid must be higher than current amount");

        this.amountOffered = amount;
        this.owner1 = owner1;
        this.owner2 = owner2;
        this.lastBidTime = LocalDateTime.now();
    }


    /**
     * Calculate the profit (or loss) based on real value versus bid amount
     * @return difference between real value and amount offered
     */
    public int calculate() {
        return realValue - amountOffered;
    }




    /**
     * Close the bid
     */
    public void close() {
        this.isOpen = false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((containerId == null) ? 0 : containerId.hashCode());
        result = prime * result + amountOffered;
        result = prime * result + realValue;
        return result;
    }

    @Override
    public boolean equals(Object o) {
        try{
            Bid newBid = (Bid) o;
            return this.containerId.equals(newBid.containerId)
                    && this.amountOffered == newBid.amountOffered
                    && this.realValue == newBid.realValue;
        }catch(ClassCastException | NullPointerException e){
            return false;
        }
    }
}



