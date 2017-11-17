package slider;

import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Component
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final CommandHandler handler;

    public WebSocketConfig(final CommandHandler handler) {
        this.handler = Objects.requireNonNull(handler);
    }

    @Override
    public void registerWebSocketHandlers(final WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/commands");
    }
}
