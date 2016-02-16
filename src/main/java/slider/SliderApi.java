package slider;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainer;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;

public class SliderApi {

    public static void main(String[] args) throws Exception {
        PathHandler path = Handlers.path();

        Undertow server = Undertow.builder().addHttpListener(8080, "0.0.0.0").setHandler(path)
                .build();
        server.start();

        ServletContainer container = ServletContainer.Factory.newInstance();

        DeploymentInfo builder = Servlets.deployment()
                .setClassLoader(SliderApi.class.getClassLoader()).setContextPath("/")
                .addWelcomePage("index.html")
                .setResourceManager(new ClassPathResourceManager(SliderApi.class.getClassLoader()))
                .addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME,
                        new WebSocketDeploymentInfo()
                                .setBuffers(new DefaultByteBufferPool(true, 100))
                                .addEndpoint(CommandHandler.class))
                .setDeploymentName("slider.war");

        DeploymentManager manager = container.addDeployment(builder);
        manager.deploy();
        path.addPrefixPath("/", manager.start());
    }
}
