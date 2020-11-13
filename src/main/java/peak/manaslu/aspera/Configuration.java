package peak.manaslu.aspera;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.client.HttpClientConfiguration;

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
    private Map<String, Map<String, String>> viewsConfiguration;

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

    @JsonProperty("views")
    public Map<String, Map<String, String>> getViewsConfiguration() {
        return viewsConfiguration;
    }

    @JsonProperty("views")
    public void setViewsConfiguration(Map<String, Map<String, String>> viewsConfiguration) {
        this.viewsConfiguration = viewsConfiguration;
    }

    @JsonProperty("aspera")
    public Map<String, String> getAsperaConfiguration() {
        return asperaConfiguration;
    }

    @JsonProperty("aspera")
    public void setAsperaConfiguration(Map<String, String> asperaConfiguration) {
        this.asperaConfiguration = asperaConfiguration;
    }

}
