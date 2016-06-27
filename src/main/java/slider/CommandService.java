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
import java.io.IOException;
import java.io.StringWriter;
import java.util.Base64;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.json.Json;
import javax.json.stream.JsonGenerator;

public class CommandService {

    private Robot robot;
    private int width;
    private int height;
    private DisplayMode dm;

    public String connected() throws Exception {
        robot = new Robot();
        dm = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice()
                .getDisplayMode();
        return writeJsonObject(gen -> {
            gen.write("type", "init");
        });
    }

    public String left() throws Exception {
        key(KeyEvent.VK_LEFT);
        TimeUnit.MILLISECONDS.sleep(300);
        return screenshot();
    }

    public String right() throws Exception {
        key(KeyEvent.VK_RIGHT);
        TimeUnit.MILLISECONDS.sleep(300);
        return screenshot();
    }

    public String presentation() throws Exception {
        key(KeyEvent.VK_P);
        TimeUnit.MILLISECONDS.sleep(300);
        return screenshot();
    }

    public String resize(String command) throws Exception {
        width = Integer.parseInt(command);
        height = width * dm.getHeight() / dm.getWidth();
        return writeJsonObject(gen -> {
            gen.write("type", "resize");
            gen.write("width", width);
            gen.write("height", height);
        });
    }

    private void key(int keycode) {
        robot.keyPress(keycode);
        robot.waitForIdle();
        robot.keyRelease(keycode);
        robot.waitForIdle();
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

        return writeJsonObject(gen -> {
            gen.write("type", "screenshot");
            gen.write("data", data);
        });
    }

    private String writeJsonObject(Consumer<JsonGenerator> consumer) {
        StringWriter json = new StringWriter();
        try (JsonGenerator gen = Json.createGenerator(json)) {
            gen.writeStartObject();
            consumer.accept(gen);
            gen.writeEnd();
        }
        return json.toString();
    }
}
