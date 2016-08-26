package slider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class CommandHandler extends TextWebSocketHandler {

    @Autowired
    private CommandService service;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sendCommand(session, service.connected());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        sendCommand(session, createCommand(message.getPayload()));
    }

    private Command createCommand(String command) throws Exception {
        switch (command) {
        case "LEFT":
            return service.left();
        case "RIGHT":
            return service.right();
        case "PRESENTATION":
            return service.presentation();
        case "SCREENSHOT":
            return service.screenshot();
        default:
            return service.resize(command);
        }
    }

    private void sendCommand(WebSocketSession session, Command command) throws Exception {
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(command)));
    }
}
