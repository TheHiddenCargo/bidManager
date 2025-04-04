package arsw.tamaltolimense.bidservice.classes;


import arsw.tamaltolimense.bidservice.exception.BidException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;



@Getter
public class Bid{

    private final String containerId;
    @Setter private int amountOffered;
    private final int realValue;
    @Setter private String owner1;
    @Setter private String owner2;
    private boolean isOpen;

    /**
     * Bid's constructor
     * @param containerId, container's id being bet on
     * @param initialValue, initial value of the container
     */
    public Bid(String containerId,int initialValue, int realValue) {

        this.containerId = containerId;
        this.amountOffered = initialValue;
        this.isOpen = true;
        this.realValue = realValue;
    }

    /**
     * Change bid instance attributes
     * @param owner1, person that makes the bet
     * @param owner2, second person that participate in the bet it can be null
     * @param amount, extra money that is offer for the
     * @throws BidException if the amount offered is 0 or less
     */
    public void bet(String owner1, String owner2, int amount)throws BidException{
        if(!isOpen) throw new BidException(BidException.CLOSE);
        if(amount <= 0) throw new BidException(BidException.ZERO_AMOUNT);
        this.amountOffered += amount;
        this.owner1 = owner1;
        this.owner2 = owner2;
    }

    public int calculate(){
        return realValue - amountOffered;
    }

    /**
     * Close the bid
     */
    public void close(){
        this.isOpen = false;
    }

}
