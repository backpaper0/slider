package slider;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/commands")
public class CommandHandler {

    private final CommandService service = new CommandService();

    @OnOpen
    public void connected(Session session) throws Exception {
        session.getAsyncRemote().sendText(service.connected());
    }

    @OnMessage
    public void post(String command, Session session) throws Exception {
        session.getAsyncRemote().sendText(service.post(command));
    }
}
