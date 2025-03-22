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



    public Bid(String containerId) {
        this.creation = LocalDateTime.now();
        this.containerId = containerId;
        this.amountOffered = 0;
    }

    public void bet(String owner1, String owner2, int amount) {
        this.amountOffered += amount;
        this.owner1 = owner1;
        this.owner2 = owner2;


    }



    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + creation.hashCode();
        result = prime * result + ((containerId == null) ? 0 : containerId.hashCode());
        result = prime * result + amountOffered;
        result = prime * result + ((owner1 == null) ? 0 : owner1.hashCode());
        result = prime * result + ((owner2 == null) ? 0 : owner2.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj){
        if(obj == null || obj.getClass() != this.getClass()) return false;
        Bid bid = (Bid) obj;
        return  (this.containerId == null ? bid.getContainerId() == null :this.containerId.equals(bid.getContainerId()))
                && (this.getAmountOffered() == 0 ? bid.getAmountOffered() == 0 :this.getAmountOffered() == bid.getAmountOffered())
                && (this.owner1 == null ? bid.getOwner1() == null :this.owner1.equals(bid.getOwner1()))
                && (this.owner2 == null ? bid.getOwner2() == null :this.getOwner2().equals(bid.getOwner2()));
    }
}
