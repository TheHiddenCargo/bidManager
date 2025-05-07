package arsw.tamaltolimense.bidservice.service.impl;


import arsw.tamaltolimense.bidservice.classes.Bid;
import arsw.tamaltolimense.bidservice.exception.BidException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BidServiceTest{

    @InjectMocks
    private BidServiceImpl bidService;





    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test void shouldStartBet(){
        try {
            bidService.startBet("1",500,2000);
            assertNotNull(bidService.getBidByContainer("1"));
            assertTrue(bidService.getBidByContainer("1").isOpen());
            assertEquals(new Bid("1",500,2000),
                    bidService.getBidByContainer("1"));
        } catch (BidException e) {
            fail(e.getMessage());
        }
    }

    @Test void shouldNotStartSameBet(){

        try{
            bidService.startBet("1",500,2000);
        }catch (BidException e){
            fail(e.getMessage());
        }

        try {
            bidService.startBet("1",1000,2000);
            fail("Should throw BidException");
        } catch (BidException e) {
            assertEquals(BidException.BID_EXISTS + "1", e.getMessage());
            assertEquals(1, bidService.getAllActiveBids().size());
        }
    }

    @Test void shouldNotStartNullContainer(){
        try {
            bidService.startBet(null,500,2000);
            fail("Should throw BidException");
        } catch (BidException e) {
            assertEquals(BidException.NULL_VALUE,e.getMessage());
            assertEquals(0, bidService.getAllActiveBids().size());
        }
    }

    @Test void shouldNotStartEmptyContainer(){
        try {
            bidService.startBet("",500,2000);
            fail("Should throw BidException");
        } catch (BidException e) {
            assertEquals(BidException.NULL_VALUE,e.getMessage());
            assertEquals(0, bidService.getAllActiveBids().size());
        }
    }

    @Test
    void shouldNotHaveNegativeInitialValue(){
        try {
            bidService.startBet("1",-500,2000);
            fail("Should throw BidException");
        } catch (BidException e) {
            assertEquals(BidException.NEGATIVE_VALUE,e.getMessage());
            assertEquals(0, bidService.getAllActiveBids().size());
            assertNull(bidService.getBidByContainer("1"));
        }
    }

    @Test
    void shouldNotHaveNegativeRealValue(){
        try {
            bidService.startBet("1",500,-2000);
            fail("Should throw BidException");
        } catch (BidException e) {
            assertEquals(BidException.NEGATIVE_VALUE,e.getMessage());
            assertEquals(0, bidService.getAllActiveBids().size());
            assertNull(bidService.getBidByContainer("1"));
        }
    }

    @Test
    void shouldNotBeGreaterThanRealValue(){
        try {
            bidService.startBet("1",2001,2000);
            fail("Should throw BidException");
        } catch (BidException e) {
            assertEquals(BidException.GREATER_VALUE,e.getMessage());
            assertEquals(0, bidService.getAllActiveBids().size());
            assertNull(bidService.getBidByContainer("1"));
        }
    }

    @Test
    void shouldPlaceBid(){
        try{
            Bid currentBid = bidService.startBet("container1",100,500);

            bidService.placeBid("container1","Juan",null,200);
            bidService.placeBid("container1","Santiago",null,205);

            assertEquals(205,currentBid.getAmountOffered());
            assertEquals("Santiago",currentBid.getOwner1());

        }catch (BidException e){
            fail(e.getMessage());
        }
    }

    @Test
    void shouldNotBeLower(){
        try{
            bidService.startBet("container1",100,500);

        }catch (BidException e){
            fail(e.getMessage());
        }
        try{
            bidService.placeBid("container1","milo",null,5);
            fail("Should throw BidException");
        }catch (BidException e){
            assertEquals("New bid must be higher than current amount",e.getMessage());
            assertEquals(100, bidService.getBidByContainer("container1").getAmountOffered());
        }
    }

    @Test
    void shouldNotOfferZero(){

        try{
            bidService.startBet("container1",100,500);

        }catch (BidException e){
            fail(e.getMessage());
        }
        try{
            bidService.placeBid("container1","milo",null,0);
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.ZERO_AMOUNT,e.getMessage());
        }
    }

    @Test
    void shouldNotOfferNegative(){
        try{
            bidService.startBet("container1",100,500);

        }catch (BidException e){
            fail(e.getMessage());
        }
        try{
            bidService.placeBid("container1","milo",null,-600);
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.ZERO_AMOUNT,e.getMessage());
        }
    }

    @Test
    void ownerShouldNotBeNull(){

        try{
            bidService.startBet("container1",100,500);

        }catch (BidException e){
            fail(e.getMessage());
        }

        try{
            bidService.placeBid("container1",null,null,-600);
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.NULL_OWNER,e.getMessage());
        }
    }

    @Test
    void ownerShouldNotBeEmpty(){
        try{
            bidService.startBet("container1",100,500);

        }catch (BidException e){
            fail(e.getMessage());
        }
        try{
            bidService.placeBid("container1"," ",null,600);
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.NULL_OWNER,e.getMessage());
        }
    }

    @Test
    void shouldOfferInPairs(){

        try{
            bidService.startBet("container1",100,500);

        }catch (BidException e){
            fail(e.getMessage());
        }
        try{
            bidService.placeBid("container1","Juan",null,300);
            bidService.placeBid("container1","Santiago",null,305);
            bidService.placeBid("container1","Santiago","Juan",608);
            Bid currentBid = bidService.getBidByContainer("container1");
            assertEquals(608,currentBid.getAmountOffered());
            assertEquals("Santiago",currentBid.getOwner1());
            assertEquals("Juan",currentBid.getOwner2());

        }catch (BidException e){
            fail(e.getMessage());
        }
    }

    @Test
    void shouldNotBeLowerInPairs(){
        try{
            bidService.startBet("container1",100,500);

        }catch (BidException e){
            fail(e.getMessage());
        }
        try{
            bidService.placeBid("container1","milo","Santiago",5);
            fail("Should throw BidException");
        }catch (BidException e){
            assertEquals("New bid must be higher than current amount",e.getMessage());
            assertEquals(100, bidService.getBidByContainer("container1").getAmountOffered());
        }
    }

    @Test
    void shouldCLoseBid(){
        try{
            bidService.startBet("container1",100,500);
            bidService.startBet("container2",100,500);
            bidService.startBet("container3",100,500);
            bidService.closeBid("container2");
            bidService.closeBid("container1");
            assertFalse(bidService.getBidByContainer("container2").isOpen());
            assertFalse(bidService.getBidByContainer("container1").isOpen());
        }catch (BidException e){
            fail(e.getMessage());
        }
    }

    @Test
    void shouldNotCLoseBid(){
        try{
            bidService.closeBid("container1");
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals("No active bid found for container: container1",e.getMessage() );
        }
    }

    @Test
    void shouldNotOfferZeroInPairs(){

        try{
            bidService.startBet("container1",100,500);

        }catch (BidException e){
            fail(e.getMessage());
        }
        try{
            bidService.placeBid("container1","milo","Sam",0);
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.ZERO_AMOUNT,e.getMessage());
        }
    }

    @Test
    void shouldNotOfferNegativeInPairs(){
        try{
            bidService.startBet("container1",100,500);

        }catch (BidException e){
            fail(e.getMessage());
        }
        try{
            bidService.placeBid("container1","milo","Sam",-600);
            fail("Should have thrown exception");
        }catch (BidException e){
            assertEquals(BidException.ZERO_AMOUNT,e.getMessage());
        }
    }

    @Test
    void shouldCalculate(){
        try{
            bidService.startBet("container1",100,500);

        }catch (BidException e){
            fail(e.getMessage());
        }
        try{
            bidService.placeBid("container1","Juan",null,300);
            bidService.placeBid("container1","Santiago",null,305);
            bidService.placeBid("container1","Santiago","Juan",608);
            Bid currentBid = bidService.getBidByContainer("container1");
            assertEquals(-108,currentBid.calculate());

        }catch (BidException e){
            fail(e.getMessage());
        }
    }








}