package slider;

import java.io.IOException;
import java.io.Serializable;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Command implements Serializable {

    public final String type;

    public void send(WebSocketSession session, ObjectMapper objectMapper) {
        try {
            String json = objectMapper.writeValueAsString(this);
            TextMessage message = new TextMessage(json);
            session.sendMessage(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Command create(CommandService service, String command) {
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

    public Command(String type) {
        this.type = type;
    }

    public static class InitCommand extends Command {
        public InitCommand() {
            super("init");
        }
    }

    public static class ScreenshotCommand extends Command {
        public final String data;
        public ScreenshotCommand(String data) {
            super("screenshot");
            this.data = data;
        }
    }

    public static class ResizeCommand extends Command {
        public final int width;
        public final int height;
        public ResizeCommand(int width, int height) {
            super("resize");
            this.width = width;
            this.height = height;
        }
    }
}
