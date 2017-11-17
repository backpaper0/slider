package slider;

import java.awt.event.KeyEvent;
import java.util.Objects;

import org.springframework.stereotype.Component;

import slider.Command.InitCommand;
import slider.Command.ResizeCommand;
import slider.Command.ScreenshotCommand;

@Component
public class CommandService {

    private final CommandContext context;

    public CommandService(final CommandContext context) {
        this.context = Objects.requireNonNull(context);
    }

    public Command connected() {
        context.connected();
        return new InitCommand();
    }

    public Command left() {
        context.key(KeyEvent.VK_LEFT);
        return screenshot();
    }

    public Command right() {
        context.key(KeyEvent.VK_RIGHT);
        return screenshot();
    }

    public Command presentation() {
        context.key(KeyEvent.VK_P);
        return screenshot();
    }

    public Command resize(final String command) {
        final int width = Integer.parseInt(command);
        context.resize(width);
        return new ResizeCommand(context.getWidth(), context.getHeight());
    }

    public Command screenshot() {
        final String data = context.screenshot();
        return new ScreenshotCommand(data);
    }
}
