package slider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@SpringBootApplication
@EnableWebSocket
public class Slider implements WebSocketConfigurer {

    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(Slider.class);
        app.setHeadless(false);
        app.run(args);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(commandHandler(), "/commands");
    }

    @Bean
    public WebSocketHandler commandHandler() {
        return new CommandHandler();
    }
}
