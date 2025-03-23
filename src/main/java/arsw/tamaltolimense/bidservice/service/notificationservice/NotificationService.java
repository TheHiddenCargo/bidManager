package arsw.tamaltolimense.bidservice.service.notificationservice;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {

    @Getter
    private static final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    /**
     * Creates a connection and save it
     * @param userNickName, client
     * @param sseEmitter, SSE emitter
     */
    public void addEmitter(String userNickName, SseEmitter sseEmitter){
        sseEmitters.put(userNickName, sseEmitter);
    }


    /**
     * Send a message to the user
     * @param userNickName, client
     * @param message, information  that wants to be transfer
     */
    public static void sendMessage(String userNickName, String message) {
        SseEmitter sseEmitter = sseEmitters.get(userNickName);
        if (sseEmitter != null) {
            try {
                sseEmitter.send(SseEmitter.event().name("notification").data(message));
            } catch (IOException e) {
                sseEmitters.remove(userNickName);
            }
        }
    }

    public void removeEmitter(String userNickName) {
        sseEmitters.remove(userNickName);
    }

    public SseEmitter getEmitter(String userNickName) {
        return sseEmitters.get(userNickName);
    }
}
