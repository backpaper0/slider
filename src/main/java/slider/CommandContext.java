package slider;

import java.awt.AWTException;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

public class CommandContext {

    private Robot robot;
    private int width;
    private int height;
    private DisplayMode dm;

    public void connected() throws AWTException {
        robot = new Robot();
        dm = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDisplayMode();
    }

    public void key(int keycode) {
        robot.keyPress(keycode);
        robot.waitForIdle();
        robot.keyRelease(keycode);
        robot.waitForIdle();
    }

    public void resize(int width) {
        this.width = width;
        this.height = width * dm.getHeight() / dm.getWidth();
    }

    public String screenshot() throws IOException {
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
        return data;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
