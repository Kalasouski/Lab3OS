import java.net.URI;
import java.net.http.*;

public class Client {
    static String requestData ="{\"username\" : \"user\", \"password\" : \"69\"}";
    private static String[] args;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public static void main(String[] args) {
    }

    public HttpResponse<String>  sendPostLogin(String input) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(input))
                .uri(URI.create("http://localhost:9991/login"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }

    public HttpResponse<String>  sendPostRegister(String input) throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(input))
                .uri(URI.create("http://localhost:9991/register"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
                .header("Content-Type", "application/json")
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return response;

    }
}