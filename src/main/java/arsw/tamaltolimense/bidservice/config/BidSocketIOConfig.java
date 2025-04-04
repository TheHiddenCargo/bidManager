package arsw.tamaltolimense.bidservice.config;

import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BidSocketIOConfig extends BaseSocketIOConfig{


    @Value("${socket-server.bid.host}")
    private String host;

    @Value("${socket-server.bid.port}")
    private Integer port;

    @Value("${socket-server.origin}")
    private String origin;

    @Bean(name = "bidSocketIOServer")
    public SocketIOServer bidSocketIOServer() {
        return createSocketServer(host, port, origin);
    }

}
