package arsw.tamaltolimense.bidservice.exception;

public class BidException extends Exception{

    public static final String EXCEED_LIMIT = "The amount exceeds the limit";
    public static final String ZERO_AMOUNT = "Amount cannot be zero or negative";
    public static final String NULL_VALUE = "Value cannot be null";
    public static final String NEGATIVE_VALUE = "Value cannot be negative";
    public static final String NULL_OWNER = "Owner cannot be null";
    public static final String NULL_OWNERS = "Owners cannot be null";
    public static final String EXCEED_OWNER = "The amount exceeds the limit of one of the owners";
    public static final String CLOSE = "bid is closed";
    public static final String GREATER_VALUE = "Real value must be greater than initial value";
    public static final String BID_EXISTS = "A bid already exists for container the container ";
    public static final String BID_NOT_EXISTS = "No active bid found for container: ";
    public BidException(String message) {
        super(message);
    }
}
