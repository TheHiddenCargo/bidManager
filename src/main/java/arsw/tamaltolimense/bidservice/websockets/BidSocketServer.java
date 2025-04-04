package arsw.tamaltolimense.bidservice.websockets;

import arsw.tamaltolimense.bidservice.classes.Bid;
import arsw.tamaltolimense.bidservice.service.BidService;

import com.corundumstudio.socketio.SocketIOServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BidSocketServer implements CommandLineRunner {
    private final SocketIOServer server;
    private final BidService bidService;
    private static final String CONTAINER = "container";

    @Autowired
    public BidSocketServer(@Qualifier("bidSocketIOServer") SocketIOServer server, BidService bidService) {
        this.server = server;
        this.bidService = bidService;
    }


    @Override
    public void run(String... args) throws Exception {
        server.start();
        System.out.println("Servidor Socket.IO Bids iniciado en el puerto " + server.getConfiguration().getPort());

        server.addConnectListener(client -> {
            String container = client.getHandshakeData().getSingleUrlParam(CONTAINER);
            System.out.println("Cliente conectado al contenedor: " + container);
            client.joinRoom(container);
        });

        server.addEventListener("offer",Map.class,(client,data,ackRequest) ->{
            Bid bid = bidService.convertToBid((Map<String,Object>)data.get("bid"));
            String container = bid.getContainerId();
            int amount = (int) data.get("amount");
            int limit = (int) data.get("limit");
            String newOwner = (String) data.get("owner");
            bidService.offer(amount,limit,bid,newOwner);
            Map<String, Bid> newBid = new HashMap<>();
            newBid.put("bid",bid);
            server.getRoomOperations(container).sendEvent("adjust_price", newBid);
            if (ackRequest.isAckRequested()) ackRequest.sendAckData("Offer request processed successfully");
        });

        server.addEventListener("pair_offer",Map.class,(client,data,ackRequest) ->{
            Bid bid = bidService.convertToBid((Map<String,Object>)data.get("bid"));
            String container = bid.getContainerId();
            int amount = (int) data.get("amount");
            int limit1 = (int) data.get("limit1");
            int limit2 = (int) data.get("limit2");
            String newOwner1 = (String) data.get("owner1");
            String newOwner2 = (String) data.get("owner2");
            bidService.offerInPairs(amount,limit1,limit2,bid,newOwner1,newOwner2);
            Map<String, Bid> newBid = new HashMap<>();
            newBid.put("bid",bid);
            server.getRoomOperations(container).sendEvent("adjust_price", newBid);
            if (ackRequest.isAckRequested()) ackRequest.sendAckData("Pair offer request processed successfully");
        });

        server.addDisconnectListener(client -> {
            String container = client.getHandshakeData().getSingleUrlParam(CONTAINER);
            System.out.println("Cliente desconectado del contenedor: " + container + client.getSessionId());
        });


        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));








    }
}
