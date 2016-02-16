package slider;

import java.awt.Robot;
import java.awt.event.KeyEvent;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/commands")
public class CommandHandler {

    @OnMessage
    public void post(String command, Session session) throws Exception {
        Robot robot = new Robot();
        switch (command) {
        case "LEFT":
            robot.keyPress(KeyEvent.VK_LEFT);
            break;
        case "RIGHT":
            robot.keyPress(KeyEvent.VK_RIGHT);
            break;
        }
    }
}
