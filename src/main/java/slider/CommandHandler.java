package slider;

import java.awt.Robot;
import java.awt.event.KeyEvent;

import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("commands")
public class CommandHandler {

    @POST
    public void post(String command) throws Exception {
        Robot robot = new Robot();
        switch (command.toLowerCase()) {
        case "left":
            robot.keyPress(KeyEvent.VK_LEFT);
            break;
        case "right":
            robot.keyPress(KeyEvent.VK_RIGHT);
            break;
        }
    }
}
