package peak.manaslu.aspera.resources;

import com.codahale.metrics.annotation.Timed;
import peak.manaslu.aspera.views.View;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/")
@Produces({MediaType.TEXT_HTML})
public class IndexRestController {

    private final String applicationContextPath;

    public IndexRestController(String applicationContextPath) {
        this.applicationContextPath = applicationContextPath;
    }

    @GET
    @Timed
    public Response home() {
        return Response.seeOther(URI.create("/index.html")).build();
    }

    @GET
    @Timed
    @Path("index.html")
    public View index() {
        return new View.Builder().basePath(applicationContextPath).templateKey("index").build();
    }

}
