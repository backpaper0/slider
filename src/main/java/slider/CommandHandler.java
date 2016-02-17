package slider;

import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/commands")
public class CommandHandler {

    @OnMessage
    public void post(String command, Session session) throws Exception {
        String[] commands = command.split(",");
        Robot robot = new Robot();
        switch (commands[0]) {
        case "LEFT":
            robot.keyPress(KeyEvent.VK_LEFT);
            break;
        case "RIGHT":
            robot.keyPress(KeyEvent.VK_RIGHT);
            break;
        }
        DisplayMode dm = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDisplayMode();
        Rectangle r = new Rectangle(0, 0, dm.getWidth(), dm.getHeight());
        BufferedImage src = robot.createScreenCapture(r);
        int width = Integer.parseInt(commands[1]);
        int height = width * src.getHeight() / src.getWidth();
        BufferedImage dest = new BufferedImage(width, height, src.getType());
        Graphics2D g = dest.createGraphics();
        Image img = src.getScaledInstance(width, height, BufferedImage.SCALE_FAST);
        g.drawImage(img, 0, 0, width, height, null);
        g.dispose();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(dest, "jpeg", out);
        String data = "data:image/jpeg;base64,"
                + Base64.getEncoder().encodeToString(out.toByteArray());
        session.getAsyncRemote().sendText(data);
    }
}
