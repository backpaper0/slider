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
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/commands")
public class CommandHandler {

    private Robot robot;
    private int width;
    private int height;
    private DisplayMode dm;

    @OnOpen
    public void connected(Session session) throws Exception {
        session.getAsyncRemote().sendText("{\"type\":\"connected\"}");
        robot = new Robot();
        dm = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDisplayMode();
    }

    @OnMessage
    public void post(String command, Session session) throws Exception {
        switch (command) {
        case "LEFT":
            robot.keyPress(KeyEvent.VK_LEFT);
            robot.waitForIdle();
            robot.keyRelease(KeyEvent.VK_LEFT);
            robot.waitForIdle();
            TimeUnit.MILLISECONDS.sleep(300);
            break;
        case "RIGHT":
            robot.keyPress(KeyEvent.VK_RIGHT);
            robot.waitForIdle();
            robot.keyRelease(KeyEvent.VK_RIGHT);
            robot.waitForIdle();
            TimeUnit.MILLISECONDS.sleep(300);
            break;
        case "SCREENSHOT":
            break;
        default:
            width = Integer.parseInt(command);
            height = width * dm.getHeight() / dm.getWidth();
            session.getAsyncRemote().sendText(
                    "{\"type\":\"init\",\"width\":" + width + ",\"height\":" + height + "}");
            return;
        }

        Rectangle r = new Rectangle(0, 0, dm.getWidth(), dm.getHeight());
        BufferedImage src = robot.createScreenCapture(r);
        BufferedImage dest = new BufferedImage(width, height, src.getType());
        Graphics2D g = dest.createGraphics();
        Image img = src.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
        g.drawImage(img, 0, 0, width, height, null);
        g.dispose();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(dest, "jpeg", out);
        String data = "data:image/jpeg;base64,"
                + Base64.getEncoder().encodeToString(out.toByteArray());
        session.getAsyncRemote().sendText("{\"type\":\"data\",\"data\":\"" + data + "\"}");
    }
}
