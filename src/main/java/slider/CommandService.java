package slider;

import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.StringWriter;
import java.util.function.Consumer;

import javax.json.Json;
import javax.json.stream.JsonGenerator;

public class CommandService {

    private CommandContext context = new CommandContext();

    public String connected() throws AWTException {
        context.connected();
        return writeJsonObject(gen -> {
            gen.write("type", "init");
        });
    }

    public String left() throws IOException, InterruptedException {
        context.key(KeyEvent.VK_LEFT);
        return screenshot();
    }

    public String right() throws IOException, InterruptedException {
        context.key(KeyEvent.VK_RIGHT);
        return screenshot();
    }

    public String presentation() throws IOException, InterruptedException {
        context.key(KeyEvent.VK_P);
        return screenshot();
    }

    public String resize(String command) {
        int width = Integer.parseInt(command);
        context.resize(width);
        return writeJsonObject(gen -> {
            gen.write("type", "resize");
            gen.write("width", context.getWidth());
            gen.write("height", context.getHeight());
        });
    }

    public String screenshot() throws IOException {
        String data = context.screenshot();
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
