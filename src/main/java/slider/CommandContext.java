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
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

@Component
public class CommandContext {

    private Robot robot;
    private int width;
    private int height;
    private DisplayMode dm;

    public void connected() {
        try {
            robot = new Robot();
        } catch (final AWTException e) {
            throw new RuntimeException(e);
        }
        dm = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDisplayMode();
    }

    public void key(final int keycode) {
        robot.keyPress(keycode);
        robot.waitForIdle();
        robot.keyRelease(keycode);
        robot.waitForIdle();
        try {
            TimeUnit.MILLISECONDS.sleep(300);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void resize(final int width) {
        this.width = width;
        this.height = width * dm.getHeight() / dm.getWidth();
    }

    public String screenshot() {
        final Rectangle r = new Rectangle(0, 0, dm.getWidth(), dm.getHeight());
        final BufferedImage src = robot.createScreenCapture(r);
        final BufferedImage dest = new BufferedImage(width, height, src.getType());
        final Graphics2D g = dest.createGraphics();
        final Image img = src.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
        g.drawImage(img, 0, 0, width, height, null);
        g.dispose();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            ImageIO.write(dest, "jpeg", out);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
        final String data = "data:image/jpeg;base64,"
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
