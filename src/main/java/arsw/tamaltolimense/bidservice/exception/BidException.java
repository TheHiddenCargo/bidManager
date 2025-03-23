package arsw.tamaltolimense.bidservice.exception;

public class BidException extends Exception{

    public static final String EXCEED_LIMIT = "The amount exceeds the limit";
    public static final String ZERO_AMOUNT = "Amount cannot be zero or negative";
    public static final String NULL_VALUE = "Value cannot be null";
    public static final String NEGATIVE_VALUE = "Value cannot be negative";
    public static final String NULL_OWNER = "Owner cannot be null";
    public static final String NULL_OWNERS = "Owners cannot be null";
    public static final String EXCEED_OWNER = "The amount exceeds the limit of one of the owners";
    public BidException(String message) {
        super(message);
    }
}
