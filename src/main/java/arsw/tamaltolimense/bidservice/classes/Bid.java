package arsw.tamaltolimense.bidservice.classes;


import arsw.tamaltolimense.bidservice.exception.BidException;
import lombok.Getter;

import java.time.LocalDateTime;




public class Bid{
    @Getter private final LocalDateTime creation;
    @Getter private final String containerId;
    @Getter private int amountOffered;
    @Getter  private String owner1;
    @Getter  private String owner2;

    /**
     * Bid's constructor
     * @param containerId, container's id being bet on
     * @param initialValue, initial value of the container
     */
    public Bid(String containerId,int initialValue) {
        this.creation = LocalDateTime.now();
        this.containerId = containerId;
        this.amountOffered = initialValue;
    }

    /**
     * Change bid instance attributes
     * @param owner1, person that makes the bet
     * @param owner2, second person that participate in the bet it can be null
     * @param amount, extra money that is offer for the
     * @throws BidException if the amount offered is 0 or less
     */
    public void bet(String owner1, String owner2, int amount)throws BidException{
        if(amount <= 0) throw new BidException(BidException.ZERO_AMOUNT);
        this.amountOffered += amount;
        this.owner1 = owner1;
        this.owner2 = owner2;
    }

}
