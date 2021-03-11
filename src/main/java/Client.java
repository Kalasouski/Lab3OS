import java.net.URI;
import java.net.URLEncoder;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Client {


  private final HttpClient httpClient = HttpClient.newBuilder()
          .version(HttpClient.Version.HTTP_2)
          .build();

  public static void main(String[] args) throws Exception {

    Client obj = new Client();

   // System.out.println("Testing 1 - Send Http GET request");
   // obj.sendGet();

    System.out.println("Testing 2 - Send Http POST request");
    obj.sendPost();

  }

  private void sendGet() throws Exception {

    HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create("https://httpbin.org/get"))
            .setHeader("User-Agent", "Java 11 HttpClient Bot")
            .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    // print status code
    System.out.println(response.statusCode());

    // print response body
    System.out.println(response.body());

  }

  private void sendPost() throws Exception {

    // form parameters


    HttpRequest request = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString("{\"username\" : \"user\", \"password\" : \"69\"}"))
            .uri(URI.create("http://localhost:9991/login"))
            .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
            .header("Content-Type", "application/json")
            .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    // print status code
    System.out.println(response.statusCode());

    // print response body
    System.out.println(response.body());

  }

  private static HttpRequest.BodyPublisher buildFormDataFromMap(Map<Object, Object> data) {
    var builder = new StringBuilder();
    for (Map.Entry<Object, Object> entry : data.entrySet()) {
      if (builder.length() > 0) {
        builder.append("&");
      }
      builder.append(URLEncoder.encode(entry.getKey().toString(), StandardCharsets.UTF_8));
      builder.append("=");
      builder.append(URLEncoder.encode(entry.getValue().toString(), StandardCharsets.UTF_8));
    }
    System.out.println(builder.toString());
    return HttpRequest.BodyPublishers.ofString(builder.toString());
  }

}
