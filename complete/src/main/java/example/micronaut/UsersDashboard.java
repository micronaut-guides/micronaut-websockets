package example.micronaut;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.security.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.websocket.CloseReason;
import io.micronaut.websocket.WebSocketSession;
import io.micronaut.websocket.annotation.OnClose;
import io.micronaut.websocket.annotation.OnError;
import io.micronaut.websocket.annotation.OnMessage;
import io.micronaut.websocket.annotation.OnOpen;
import io.micronaut.websocket.annotation.ServerWebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Secured(SecurityRule.IS_AUTHENTICATED)
@ServerWebSocket("/ws/users")
public class UsersDashboard implements ApplicationEventListener<UserRegisteredEvent> {

    private final UserRepository userRepository;

    public UsersDashboard(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private static final Logger LOG = LoggerFactory.getLogger(UsersDashboard.class);

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void onApplicationEvent(UserRegisteredEvent event) {
        LOG.info("User Registered");

        LOG.info("brodcasting message to {}", sessions.size());

        String message = String.valueOf(userRepository.count());
        for ( String webSocketSessionId : sessions.keySet()) {
            publishMessage(message, sessions.get(webSocketSessionId));
        }
    }

    @OnOpen
    public void onOpen(WebSocketSession session) {

        LOG.info("on Open");
        sessions.put(session.getId(), session);
        String message = String.valueOf(userRepository.count());
        publishMessage(message, session);
    }

    @OnMessage
    public void onMessage(String message, WebSocketSession session) {
        LOG.info("on Message {}", message);
    }

    public void publishMessage(String message, WebSocketSession session) {
        session.broadcastSync(message);
    }

    @OnError
    public void onError(Throwable error) {
        LOG.info("on Error");
    }

    @OnClose
    public void onClose(CloseReason closeReason, WebSocketSession session) {
        LOG.info("on Close");
        session.remove(session.getId());
    }
}