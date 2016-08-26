package slider;

import java.awt.AWTException;
import java.awt.event.KeyEvent;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import slider.Command.InitCommand;
import slider.Command.ResizeCommand;
import slider.Command.ScreenshotCommand;

@Component
public class CommandService {

    @Autowired
    private CommandContext context;

    public Command connected() throws AWTException {
        context.connected();
        return new InitCommand();
    }

    public Command left() throws IOException, InterruptedException {
        context.key(KeyEvent.VK_LEFT);
        return screenshot();
    }

    public Command right() throws IOException, InterruptedException {
        context.key(KeyEvent.VK_RIGHT);
        return screenshot();
    }

    public Command presentation() throws IOException, InterruptedException {
        context.key(KeyEvent.VK_P);
        return screenshot();
    }

    public Command resize(String command) {
        int width = Integer.parseInt(command);
        context.resize(width);
        return new ResizeCommand(context.getWidth(), context.getHeight());
    }

    public Command screenshot() throws IOException {
        String data = context.screenshot();
        return new ScreenshotCommand(data);
    }
}
