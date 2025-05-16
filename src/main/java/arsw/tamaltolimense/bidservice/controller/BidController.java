package arsw.tamaltolimense.bidservice.controller;

import arsw.tamaltolimense.bidservice.classes.Bid;
import arsw.tamaltolimense.bidservice.exception.BidException;
import arsw.tamaltolimense.bidservice.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/bids")
public class BidController {

    private static final Logger logger = Logger.getLogger(BidController.class.getName());

    private final BidService bidService;

    @Autowired
    public BidController(BidService bidService) {
        this.bidService = bidService;
    }

    /**
     * Start a new bid for a container - acepta parámetros tanto en el cuerpo JSON como en la URL
     */
    @PostMapping("/start")
    public ResponseEntity<Object> createBid(@RequestBody(required = false) Map<String, String> requestBody,
                                            @RequestParam(required = false) Map<String, String> requestParams) {
        try {
            // Combinar parámetros de cuerpo y URL, priorizando los del cuerpo
            Map<String, String> allParams = new java.util.HashMap<>(requestParams);
            if (requestBody != null) {
                allParams.putAll(requestBody);
            }

            // Log para depuración
            logger.info("Parámetros recibidos en /start: " + allParams);

            String container = allParams.get("container");
            if (container == null) {
                // Intentar con nombres alternativos
                container = allParams.get("idContainer");
            }

            if (container == null) {
                return new ResponseEntity<>("Falta el parámetro 'container' o 'idContainer'", HttpStatus.BAD_REQUEST);
            }

            String initialValueStr = allParams.get("initialValue");
            if (initialValueStr == null) {
                return new ResponseEntity<>("Falta el parámetro 'initialValue'", HttpStatus.BAD_REQUEST);
            }

            int initialValue = Integer.parseInt(initialValueStr);

            // El valor real es opcional, se establece un valor predeterminado si no está presente
            int realValue;
            String realValueStr = allParams.get("realValue");
            if (realValueStr != null) {
                realValue = Integer.parseInt(realValueStr);
            } else {
                // Valor predeterminado: el doble del valor inicial
                realValue = initialValue * 2;
            }

            Bid newBid = bidService.startBet(container, initialValue, realValue);
            return new ResponseEntity<>(newBid, HttpStatus.CREATED);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Error de formato numérico: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (BidException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
           logger.log(Level.SEVERE, e.getMessage(), e);
           return new ResponseEntity<>("Error inesperado: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Place a bid offer on a container - acepta parámetros tanto en el cuerpo JSON como en la URL
     */
    @PostMapping("/offer")
    public ResponseEntity<Object> placeBidOffer(@RequestBody(required = false) Map<String, String> requestBody,
                                                @RequestParam(required = false) Map<String, String> requestParams) {
        try {
            // Combinar parámetros de cuerpo y URL, priorizando los del cuerpo
            Map<String, String> allParams = new java.util.HashMap<>(requestParams);
            if (requestBody != null) {
                allParams.putAll(requestBody);
            }

            // Log para depuración
            logger.info("Parámetros recibidos en /offer: " + allParams);

            String container = allParams.get("container");
            if (container == null) {
                return new ResponseEntity<>("Falta el parámetro 'container'", HttpStatus.BAD_REQUEST);
            }

            String owner = allParams.get("owner");
            if (owner == null) {
                return new ResponseEntity<>("Falta el parámetro 'owner'", HttpStatus.BAD_REQUEST);
            }

            String amountStr = allParams.get("amount");
            if (amountStr == null) {
                return new ResponseEntity<>("Falta el parámetro 'amount'", HttpStatus.BAD_REQUEST);
            }

            int amount = Integer.parseInt(amountStr);

            // El segundo propietario es opcional
            String owner2 = allParams.get("owner2");

            Bid updatedBid = bidService.placeBid(container, owner, owner2, amount);
            return new ResponseEntity<>(updatedBid, HttpStatus.OK);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Error de formato numérico: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (BidException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage(), e);
            return new ResponseEntity<>("Error inesperado: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get current state of a bid for a container
     */
    @GetMapping("/{containerId}")
    public ResponseEntity<Object> getBid(@PathVariable String containerId) {
        try {
            Bid bid = bidService.getBidByContainer(containerId);
            if (bid == null) {
                return new ResponseEntity<>("No se encontró ninguna apuesta para el contenedor: " + containerId, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(bid, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al recuperar la apuesta: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Calculate the profit/loss for a bid
     */
    @GetMapping("/calculate/{containerId}")
    public ResponseEntity<Object> calculateBid(@PathVariable String containerId) {
        try {
            Bid bid = bidService.getBidByContainer(containerId);
            if (bid == null) {
                return new ResponseEntity<>("No se encontró ninguna apuesta para el contenedor: " + containerId, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(bidService.calculate(bid), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al calcular la apuesta: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Close a bid for a container
     */
    @PostMapping("/close/{containerId}")
    public ResponseEntity<Object> closeBid(@PathVariable String containerId) {
        try {
            Bid bid = bidService.closeBid(containerId);
            return new ResponseEntity<>(bid, HttpStatus.OK);
        } catch (BidException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al cerrar la apuesta: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}