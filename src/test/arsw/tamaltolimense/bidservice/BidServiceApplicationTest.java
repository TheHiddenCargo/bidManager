package arsw.tamaltolimense.bidservice;

import arsw.tamaltolimense.bidservice.classes.Bid;
import arsw.tamaltolimense.bidservice.exception.BidException;
import arsw.tamaltolimense.bidservice.service.bidservice.impl.BidServiceImpl;
import arsw.tamaltolimense.bidservice.service.notificationservice.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BidServiTest{

    @InjectMocks
    private BidServiceImpl bidService;

    @InjectMocks
    private NotificationService notificationService;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldOffer(){
        try{
            Bid currentBid = bidService.startBet("container1",100);

            bidService.offer(200,301,currentBid,"Juan");
            bidService.offer(5,305,currentBid,"Santiago");

            assertEquals(305,currentBid.getAmountOffered());
            assertEquals("Santiago",currentBid.getOwner1());

        }catch (BidException e){
            fail(e.getMessage());
        }
    }

    @Test
    void shouldNotStartNull(){
        try{
            bidService.startBet(null,500);
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.NULL_VALUE, e.getMessage());
        }
    }

    @Test
    void shouldNotStartEmpty() {
        try {
            bidService.startBet("              ", 500);
            fail("Should have thrown exception");
        } catch (BidException e) {
            assertEquals(BidException.NULL_VALUE, e.getMessage());
        }
    }

    @Test
    void shouldNotStartNegative() {
        try {
            bidService.startBet("container", -45);
            fail("Should have thrown exception");
        } catch (BidException e) {
            assertEquals(BidException.NEGATIVE_VALUE, e.getMessage());
        }
    }

    @Test
    void shouldNotExceedLimit(){
        try{
            bidService.offer(100,150,new Bid("container",151),"milo");
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.EXCEED_LIMIT, e.getMessage());
        }
    }
    @Test
    void shouldNotOfferZero(){
        try{
            bidService.offer(0,150,new Bid("container",150),"milo");
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.ZERO_AMOUNT,e.getMessage());
        }
    }

    @Test
    void shouldNotOfferNegative(){
        try{
            bidService.offer(-45,150,new Bid("container",150),"milo");
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.ZERO_AMOUNT,e.getMessage());
        }
    }

    @Test
    void ownerShouldNotBeNull(){
        try{
            bidService.offer(5,150,new Bid("container",0),null);
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.NULL_OWNER,e.getMessage());
        }
    }

    @Test
    void ownerShouldNotBeEmpty(){
        try{
            bidService.offer(5,150,new Bid("container",0),"  ");
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.NULL_OWNER,e.getMessage());
        }
    }

    @Test
    void shouldOfferInPairs(){
        try{
            Bid currentBid = bidService.startBet("container1",100);

            bidService.offer(200,301,currentBid,"Juan");
            bidService.offer(5,305,currentBid,"Santiago");
            bidService.offerInPairs(300,303,305,currentBid,"Juan","Santiago");

            assertEquals(605,currentBid.getAmountOffered());
            assertEquals("Santiago",currentBid.getOwner2());
            assertEquals("Juan",currentBid.getOwner1());

        }catch (BidException e){
            fail(e.getMessage());
        }
    }


    @Test
    void shouldNotExceedPairLimit(){
        try{
            bidService.offerInPairs(100,50,40,new Bid("container",0),
                    "juan",
                    "milo");
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.EXCEED_LIMIT, e.getMessage());
        }
    }

    @Test
    void shouldNotExceedOwner1(){
        try{
            bidService.offerInPairs(200,50,500,new Bid("container",0),
                    "juan",
                    "milo");
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.EXCEED_OWNER, e.getMessage());
        }
    }

    @Test
    void shouldNotExceedOwner2(){
        try{
            bidService.offerInPairs(500,600,249,new Bid("container",0),
                    "juan",
                    "milo");
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.EXCEED_OWNER, e.getMessage());
        }
    }

    @Test
    void shouldNotOfferZeroInPairs(){
        try{
            bidService.offerInPairs(0,200,40,new Bid("container",0),
                    "juan",
                    "milo");
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.ZERO_AMOUNT,e.getMessage());
        }
    }

    @Test
    void shouldNotOfferNegativeInPairs(){
        try{
            bidService.offerInPairs(-45,200,40,new Bid("container",0),
                    "juan",
                    "milo");
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.ZERO_AMOUNT,e.getMessage());
        }
    }

    @Test
    void owner1ShouldNotBeNull(){
        try{
            bidService.offerInPairs(4,200,40,new Bid("container",0),
                    null,
                    "milo");
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.NULL_OWNERS,e.getMessage());
        }
    }

    @Test
    void owner2ShouldNotBeNull(){
        try{
            bidService.offerInPairs(4,200,40,new Bid("container",0),
                    "m",
                    null);
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.NULL_OWNERS,e.getMessage());
        }
    }

    @Test
    void owner1ShouldNotBeEmpty(){
        try{
            bidService.offerInPairs(4,200,40,new Bid("container",0),
                    "    ",
                    "milo");
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.NULL_OWNERS,e.getMessage());
        }
    }

    @Test
    void owner2ShouldNotBeEmpty(){
        try{
            bidService.offerInPairs(4,200,40,new Bid("container",0),
                    "m",
                    "    ");
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.NULL_OWNERS,e.getMessage());
        }
    }

    @Test
    void testAddEmitter() {
        String userNickName = "user1";
        SseEmitter emitter = new SseEmitter();
        notificationService.addEmitter(userNickName, emitter);
        assertNotNull(notificationService.getEmitter(userNickName));
    }

    @Test
    void testSendMessage() {
        String userNickName = "user1";
        SseEmitter emitter = new SseEmitter();
        notificationService.addEmitter(userNickName, emitter);

        assertDoesNotThrow(() -> NotificationService.sendMessage(userNickName, "Test message"));


        emitter.complete();
        try{
            NotificationService.sendMessage(userNickName, "Another message");
        }catch (IllegalStateException e){
            assertEquals("ResponseBodyEmitter has already completed", e.getMessage());
            notificationService.removeEmitter(userNickName);
        }

        assertNull(notificationService.getEmitter(userNickName));
    }

    @Test
    void testRemoveEmitter() {
        String userNickName = "user1";
        SseEmitter emitter = new SseEmitter();
        notificationService.addEmitter(userNickName, emitter);

        notificationService.removeEmitter(userNickName);
        assertNull(notificationService.getEmitter(userNickName));
    }

}