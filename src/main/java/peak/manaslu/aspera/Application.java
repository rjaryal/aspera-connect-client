package peak.manaslu.aspera;

import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.client.HttpClientBuilder;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import peak.manaslu.aspera.api.NoCacheFilter;
import peak.manaslu.aspera.resources.IndexRestController;
import peak.manaslu.aspera.resources.AsperaConnectController;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletRegistration;
import java.util.EnumSet;
import java.util.Map;

public class Application extends io.dropwizard.Application<Configuration> {

    @Override
    public void initialize(Bootstrap<Configuration> bootstrap) {
        // support link to assets
        bootstrap.addBundle(new AssetsBundle("/assets/", "/assets/", null, "assets"));

        // support multipart form submission
        bootstrap.addBundle(new MultiPartBundle());

        // support freemarker view
        bootstrap.addBundle(new ViewBundle<>());
    }

    @Override
    public void run(Configuration conf, Environment env) {
        final Map<String, String> asperaConf = conf.getAsperaConfiguration();
        final String asperaHost = asperaConf.get("remote_host");
        final int asperaPort = Integer.parseInt(asperaConf.getOrDefault("http_fallback_port", "9091"));

        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
            new AuthScope(asperaHost, asperaPort),
            new UsernamePasswordCredentials(asperaConf.get("remote_user"), asperaConf.get("remote_password"))
        );

        final CloseableHttpClient httpClient = new HttpClientBuilder(env)
                .using(conf.getHttpClientConfiguration())
                .using(credentialsProvider)
                .build(getName());

        final FilterRegistration.Dynamic cors = env.servlets().addFilter("CORS", CrossOriginFilter.class);
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        env.servlets()
                .addFilter("NoCacheFilter", NoCacheFilter.class)
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");

        env.jersey().register(new IndexRestController(env.getApplicationContext().getContextPath()));

        final ServletRegistration.Dynamic aspera = env.servlets().addServlet(
            "aspera",
            new AsperaConnectController(asperaHost, asperaPort, httpClient)
        );
        aspera.setLoadOnStartup(1);
        aspera.addMapping("/connect");
    }

    public static void main(String[] args) throws Exception {
        new Application().run(args);
    }

}
