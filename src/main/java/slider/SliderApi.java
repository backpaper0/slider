package slider;

import java.net.URI;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

public class SliderApi {

    public static void main(String[] args) throws Exception {
        URI uri = URI.create("http://localhost:8080/");
        ResourceConfig config = new ResourceConfig().register(CommandHandler.class);
        HttpServer httpServer = GrizzlyHttpServerFactory.createHttpServer(uri, config);
        Thread.sleep(Long.MAX_VALUE);
    }
}
