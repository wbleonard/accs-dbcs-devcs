package com.oracle.cloud.accs.sample;

import com.oracle.cloud.accs.sample.rest.PaasAppDevProductsResource;
import com.oracle.cloud.accs.sample.jpa.JPAFacade;
import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 * The 'bootstrap' class. Sets up persistence and starts Grizzly HTTP server
 *
 */
public class Bootstrap {

    static void bootstrapREST() throws IOException {

        String hostname = Optional.ofNullable(System.getenv("HOSTNAME")).orElse("localhost");
        String port = Optional.ofNullable(System.getenv("PORT")).orElse("8080");

        URI baseUri = UriBuilder.fromUri("http://" + hostname + "/").port(Integer.parseInt(port)).build();

        ResourceConfig config = new ResourceConfig(PaasAppDevProductsResource.class)
                                                    .register(MoxyJsonFeature.class);

        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(baseUri, config);
        Logger.getLogger(Bootstrap.class.getName()).log(Level.INFO, "Application accessible at {0}", baseUri.toString());

        //gracefully exit Grizzly and Eclipselink services when app is shut down
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                Logger.getLogger(Bootstrap.class.getName()).info("Exiting......");
                server.shutdownNow();
                JPAFacade.closeEMF();
                Logger.getLogger(Bootstrap.class.getName()).info("REST and Persistence services stopped");
            }
        }));
        server.start();

    }

    private static final String PERSISTENCE_UNIT_NAME = "oracle-cloud-db-PU";

    static void bootstrapJPA(String puName, Map<String, String> props) {

        JPAFacade.bootstrapEMF(puName, props);
        Logger.getLogger(Bootstrap.class.getName()).info("EMF bootstrapped");

    }

    public static void main(String[] args) throws IOException {
        Logger.getLogger(Bootstrap.class.getName()).log(Level.INFO, "Starting application...");
        String connectDescriptor = System.getenv("DBAAS_DEFAULT_CONNECT_DESCRIPTOR");
        String dbUser = System.getenv("PDB1_USER_NAME");
        String dbPassword = System.getenv("PDB1_USER_PASSWORD");
        Logger.getLogger(Bootstrap.class.getName()).info("DBAAS_DEFAULT_CONNECT_DESCRIPTOR: " + connectDescriptor);
        System.out.println("PDB1_USER_NAME: " + dbUser);
        //System.out.println("PDB1_USER_PASSWORD: " + dbPassword);
        Map<String, String> props = new HashMap<>();
        props.put("javax.persistence.jdbc.url", "jdbc:oracle:thin:@" + connectDescriptor);
        props.put("javax.persistence.jdbc.user", dbUser);
        props.put("javax.persistence.jdbc.password", dbPassword);
        bootstrapREST();
        bootstrapJPA(PERSISTENCE_UNIT_NAME, props);

    }
}
