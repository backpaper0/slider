package slider;

import java.io.Serializable;

public class Command implements Serializable {

    public final String type;

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
