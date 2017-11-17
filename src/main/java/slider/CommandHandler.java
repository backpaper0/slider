package slider;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CommandHandler extends TextWebSocketHandler {

    private final CommandService service;
    private final ObjectMapper objectMapper;

    public CommandHandler(final CommandService service, final ObjectMapper objectMapper) {
        this.service = Objects.requireNonNull(service);
        this.objectMapper = Objects.requireNonNull(objectMapper);
    }

    @Override
    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {
        final Command command = service.connected();
        command.send(session, objectMapper);
    }

    @Override
    protected void handleTextMessage(final WebSocketSession session, final TextMessage message)
            throws Exception {
        final Command command = Command.create(service, message.getPayload());
        command.send(session, objectMapper);
    }
}
