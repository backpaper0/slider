package slider;

import java.security.Principal;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.IntStream;

import io.undertow.Handlers;
import io.undertow.Undertow;
import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.AuthenticationMode;
import io.undertow.security.handlers.AuthenticationCallHandler;
import io.undertow.security.handlers.AuthenticationConstraintHandler;
import io.undertow.security.handlers.AuthenticationMechanismsHandler;
import io.undertow.security.handlers.SecurityInitialHandler;
import io.undertow.security.idm.Account;
import io.undertow.security.idm.Credential;
import io.undertow.security.idm.IdentityManager;
import io.undertow.security.idm.PasswordCredential;
import io.undertow.security.impl.BasicAuthenticationMechanism;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.server.HttpHandler;
import io.undertow.server.handlers.PathHandler;
import io.undertow.server.handlers.resource.ClassPathResourceManager;
import io.undertow.servlet.Servlets;
import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.servlet.api.DeploymentManager;
import io.undertow.servlet.api.ServletContainer;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;

public class Slider {

    public static void main(String[] args) throws Exception {

        PathHandler path = Handlers.path();

        int[] cs = IntStream.concat(IntStream.rangeClosed('0', '9'), IntStream.rangeClosed('a', 'z')
                .flatMap(c -> IntStream.of(c, Character.toUpperCase(c)))).toArray();
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        char[] password = new char[8];
        for (int i = 0; i < password.length; i++) {
            password[i] = (char) cs[sr.nextInt(cs.length)];
        }
        System.out.println(password);

        IdentityManager identityManager = new MapIdentityManager(password);

        HttpHandler handler = path;
        handler = new AuthenticationCallHandler(handler);
        handler = new AuthenticationConstraintHandler(handler);
        List<AuthenticationMechanism> mechanisms = Collections
                .singletonList(new BasicAuthenticationMechanism("Slider Realm"));
        handler = new AuthenticationMechanismsHandler(handler, mechanisms);
        handler = new SecurityInitialHandler(AuthenticationMode.PRO_ACTIVE, identityManager,
                handler);

        Undertow server = Undertow.builder().addHttpListener(8080, "0.0.0.0").setHandler(handler)
                .build();
        server.start();

        ServletContainer container = ServletContainer.Factory.newInstance();

        DeploymentInfo builder = Servlets.deployment().setClassLoader(Slider.class.getClassLoader())
                .setContextPath("/").addWelcomePage("index.html")
                .setResourceManager(new ClassPathResourceManager(Slider.class.getClassLoader()))
                .addServletContextAttribute(WebSocketDeploymentInfo.ATTRIBUTE_NAME,
                        new WebSocketDeploymentInfo()
                                .setBuffers(new DefaultByteBufferPool(true, 100))
                                .addEndpoint(CommandHandler.class))
                .setDeploymentName("slider.war");

        DeploymentManager manager = container.addDeployment(builder);
        manager.deploy();
        path.addPrefixPath("/", manager.start());
    }

    private static class MapIdentityManager implements IdentityManager {

        private final char[] password;

        public MapIdentityManager(char[] password) {
            this.password = password;
        }

        @Override
        public Account verify(Account account) {
            return account;
        }

        @Override
        public Account verify(String id, Credential credential) {
            if (Objects.equals(id, "slider") && credential instanceof PasswordCredential) {
                if (Arrays.equals(password, ((PasswordCredential) credential).getPassword())) {
                    return new Account() {

                        private final Principal principal = () -> id;

                        @Override
                        public Set<String> getRoles() {
                            return Collections.emptySet();
                        }

                        @Override
                        public Principal getPrincipal() {
                            return principal;
                        }
                    };
                }
            }
            return null;
        }

        @Override
        public Account verify(Credential credential) {
            return null;
        }
    }
}
