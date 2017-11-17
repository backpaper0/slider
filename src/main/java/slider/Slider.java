package slider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Slider {

    public static void main(final String[] args) throws Exception {
        final SpringApplication app = new SpringApplication(Slider.class);
        app.setHeadless(false);
        app.run(args);
    }
}
