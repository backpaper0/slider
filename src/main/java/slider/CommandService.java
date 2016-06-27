package slider;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.json.Json;
import javax.json.stream.JsonGenerator;

public class CommandService {

    private CommandContext context = new CommandContext();

    public String connected() throws Exception {
        context.connected();
        return writeJsonObject(gen -> {
            gen.write("type", "init");
        });
    }

    public String left() throws Exception {
        context.key(KeyEvent.VK_LEFT);
        TimeUnit.MILLISECONDS.sleep(300);
        return screenshot();
    }

    public String right() throws Exception {
        context.key(KeyEvent.VK_RIGHT);
        TimeUnit.MILLISECONDS.sleep(300);
        return screenshot();
    }

    public String presentation() throws Exception {
        context.key(KeyEvent.VK_P);
        TimeUnit.MILLISECONDS.sleep(300);
        return screenshot();
    }

    public String resize(String command) throws Exception {
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
