package slider;

import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Command implements Serializable {

    public final String type;

    public void send(final WebSocketSession session, final ObjectMapper objectMapper) {
        try {
            final String json = objectMapper.writeValueAsString(this);
            final TextMessage message = new TextMessage(json);
            session.sendMessage(message);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Command create(final CommandService service, final String command) {
        switch (command) {
        case "LEFT":
            return service.left();
        case "RIGHT":
            return service.right();
        case "PRESENTATION":
            return service.presentation();
        case "SCREENSHOT":
            return service.screenshot();
        default:
            return service.resize(command);
        }
    }

    public Command(final String type) {
        this.type = Objects.requireNonNull(type);
    }

    public static class InitCommand extends Command {
        public InitCommand() {
            super("init");
        }
    }

    public static class ScreenshotCommand extends Command {
        public final String data;

        public ScreenshotCommand(final String data) {
            super("screenshot");
            this.data = data;
        }
    }

    public static class ResizeCommand extends Command {
        public final int width;
        public final int height;

        public ResizeCommand(final int width, final int height) {
            super("resize");
            this.width = width;
            this.height = height;
        }
    }
}
