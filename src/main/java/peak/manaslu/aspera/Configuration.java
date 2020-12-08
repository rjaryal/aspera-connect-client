package peak.manaslu.aspera;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.client.HttpClientConfiguration;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.server.ServerFactory;
import org.apache.commons.lang3.RandomStringUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Configuration extends io.dropwizard.Configuration {

    @Valid
    @NotNull
    private HttpClientConfiguration httpClient = new HttpClientConfiguration();

    @Valid
    @NotNull
    private Map<String, String> asperaConfiguration;

    @JsonProperty("httpClient")
    public HttpClientConfiguration getHttpClientConfiguration() {
        return httpClient;
    }

    @JsonProperty("httpClient")
    public void setHttpClientConfiguration(HttpClientConfiguration httpClient) {
        this.httpClient = httpClient;
    }

    @JsonProperty("aspera")
    public Map<String, String> getAsperaConfiguration() {
        return asperaConfiguration;
    }

    @JsonProperty("aspera")
    public void setAsperaConfiguration(Map<String, String> asperaConfiguration) {
        this.asperaConfiguration = asperaConfiguration;
    }

    @Override
    @JsonProperty("server")
    public ServerFactory getServerFactory() {
        // fix erroneous rootPath
        DefaultServerFactory server = ((DefaultServerFactory) super.getServerFactory());
        final String rootPath = server.getJerseyRootPath().orElse("/");
        if (rootPath.equals("") || rootPath.equals("/")) {
            String randomRootPath = RandomStringUtils.randomAlphanumeric(7);
            server.setJerseyRootPath("/" + randomRootPath);
        }
        return server;
    }

}
