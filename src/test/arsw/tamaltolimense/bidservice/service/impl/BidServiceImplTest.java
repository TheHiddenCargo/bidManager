package arsw.tamaltolimense.bidservice.service.impl;

import arsw.tamaltolimense.bidservice.classes.Bid;
import arsw.tamaltolimense.bidservice.exception.BidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class BidServiceImplTest {
    @InjectMocks
    private BidServiceImpl bidService;



    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldOffer() throws InterruptedException, BidException {
        Bid bid = bidService.startBet("container123");
        int limit = 1000000;


        // Crear un pool de hilos
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        // Ejecutar múltiples ofertas en paralelo

        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                try {
                    bidService.offer(20, limit, bid, "newOwner");
                } catch (BidException e) {
                    fail(e.getMessage());
                }
            });
        }

        // Esperar a que todos los hilos terminen
        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.MINUTES);

        // Verificar que la cantidad ofrecida sea la esperada
        assertEquals(200, bid.getAmountOffered());

    }



}