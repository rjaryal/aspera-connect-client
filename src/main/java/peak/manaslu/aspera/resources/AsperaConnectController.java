package peak.manaslu.aspera.resources;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class AsperaConnectController extends HttpServlet {

    private final CloseableHttpClient httpClient;
    private final String host;
    private final int port;

    public AsperaConnectController(String host, int port, CloseableHttpClient httpClient) {
        this.host = host;
        this.port = port;
        this.httpClient = httpClient;
    }

    // Intercept POST Submission
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        PrintWriter out = response.getWriter();

        // DOWNLOAD
        if(request.getParameter("download") != null) {
            String spec = "{ \"transfer_requests\" : [ { \"transfer_request\" : { \"paths\" : [ { \"source\" : \"" + request.getParameter("download") + "\" } ] } } ] }";
            try {
                out.print(makeAsperaConnect("download_setup", spec));
            } catch (Exception ex) {
                out.print("{\"error\" : \"" + ex + "\"}");
            }
        }

        // UPLOAD
        else if(request.getParameter("upload") != null) {
            String spec = "{ \"transfer_requests\" : [ { \"transfer_request\" : { \"paths\" : [{}], \"destination_root\" : \"" + request.getParameter("upload") + "\" } } ] }";
            try {
                out.print(makeAsperaConnect("upload_setup", spec));
            } catch (Exception ex) {
                out.print("{\"error\" : \"" + ex + "\"}");
            }
        }

        // BROWSE
        else if(request.getParameter("changeDirectory") != null) {
            try {
                out.print(makeAsperaConnect("browse", "{ \"path\" : \"" + request.getParameter("changeDirectory") + "\" }"));
            } catch (Exception ex) {
                out.print("{\"error\" : \"" + ex + "\"}");
            }
        }

        // DELETE FILE
        else if(request.getParameter("deleteFile") != null) {
            try {
                out.print(makeAsperaConnect("delete", "{\"paths\" : [ { \"path\" : \"" + request.getParameter("deleteFile") + "\" }]}"));
            } catch (Exception ex) {
                out.print("{\"error\" : \"" + ex + "\"}");
            }
        }

        // CREATE DIR
        else if(request.getParameter("createDir") != null) {
            try {
                out.print(makeAsperaConnect("create", "{\"paths\" : [ { \"path\" : \"" + request.getParameter("createDir") + "\", \"type\" : \"directory\" }]}"));
            } catch (Exception ex) {
                out.print("{\"error\" : \"" + ex + "\"}");
            }
        }

        // STARTING DIR
        else if(request.getParameter("startingdirectory") != null) {
            out.print("/");
        }

        // RENAME PATH
        else if(request.getParameter("renamePath") != null) {
            String[] pathParts = request.getParameter("renamePath").split("/");
            String spec = "{\"paths\" : [{ \"path\" : \"" + request.getParameter("renamePath").replace(pathParts[pathParts.length-1], "") + "\", \"source\" : \"" + pathParts[pathParts.length-1] + "\", \"destination\" : \"" + request.getParameter("renameName") + "\" }]}";
            try {
                out.print(makeAsperaConnect("rename", spec));
            } catch (Exception ex) {
                out.print("{\"error\" : \"" + ex + "\"}");
            }
        }
    }

    private String makeAsperaConnect(String command, String spec) throws IOException {
        HttpPost httpPost = new HttpPost("http://" + host + ":" + port + "/files/" + command);
        httpPost.setEntity(new StringEntity(spec));
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");

        CloseableHttpResponse response = httpClient.execute(httpPost);
        String result = EntityUtils.toString(response.getEntity());
        response.close();
        return result;
    }

}
