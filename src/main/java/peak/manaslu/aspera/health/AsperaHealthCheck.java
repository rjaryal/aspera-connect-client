package peak.manaslu.aspera.health;

import com.codahale.metrics.health.HealthCheck;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

public class AsperaHealthCheck extends HealthCheck {

    private final CloseableHttpClient httpClient;
    private final String host;
    private final int port;

    public AsperaHealthCheck(String host, int port, CloseableHttpClient httpClient) {
        this.host = host;
        this.port = port;
        this.httpClient = httpClient;
    }

    public Result check() throws Exception {

        try (CloseableHttpResponse response = httpClient.execute(new HttpGet("http://" + host + ":" + port))) {
            if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                return Result.healthy(EntityUtils.toString(response.getEntity()));
            }
            return Result.unhealthy(response.getStatusLine().getReasonPhrase());
        }
    }

}
