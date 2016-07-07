package slider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class CommandHandler extends TextWebSocketHandler {

    @Autowired
    private CommandService service;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.sendMessage(new TextMessage(service.connected()));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        String command = message.getPayload();
        switch (command) {
        case "LEFT":
            session.sendMessage(new TextMessage(service.left()));
            break;
        case "RIGHT":
            session.sendMessage(new TextMessage(service.right()));
            break;
        case "PRESENTATION":
            session.sendMessage(new TextMessage(service.presentation()));
            break;
        case "SCREENSHOT":
            session.sendMessage(new TextMessage(service.screenshot()));
            break;
        default:
            session.sendMessage(new TextMessage(service.resize(command)));
            break;
        }
    }
}
