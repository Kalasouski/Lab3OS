import java.net.URI;
import java.net.http.*;

public class Client {
  static String requestData ="{\"username\" : \"user\", \"password\" : \"69\"}";

  private final static HttpClient httpClient = HttpClient.newBuilder()
          .version(HttpClient.Version.HTTP_1_1)
          .build();

  public static void main(String[] args) throws Exception {

    System.out.println("Testing 1 - Send Http POST request: register");
    printResponse(sendPostRegister(requestData));

    System.out.println("Testing 2 - Send Http POST request: login\nShould be: OK");
    printResponse(sendPostLogin(requestData));

    System.out.println("Testing 3 - Send Http GET request: logout\nShould be: OK");
    printResponse(sendGetLogout());

    System.out.println("Testing 4 - Send Http POST request: register\nWhen registered\nShould be: Not OK");
    printResponse(sendPostRegister(requestData));

    System.out.println("Testing 5 - Send Http GET request: logout\nWhen not logged in\nShould be: Not OK");
    printResponse(sendGetLogout());
  }

  private static HttpResponse<String> sendGetLogout() throws Exception {

    HttpRequest request = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create("http://localhost:9991/logout?user"))
            .setHeader("User-Agent", "Java 11 HttpClient Bot")
            .build();


    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return response;
  }

  public static void printResponse(HttpResponse<String> response){
    // print status code
    if(response.statusCode()==200)
      System.out.println("OK");
    else
      System.out.println("Not OK: "+response.statusCode());

    // print response body
    System.out.println(response.body());
  }

  private static HttpResponse<String>  sendPostLogin(String input) throws Exception {

    // form parameters

    HttpRequest request = HttpRequest.newBuilder()
            .POST(HttpRequest.BodyPublishers.ofString(input))
            .uri(URI.create("http://localhost:9991/login"))
            .setHeader("User-Agent", "Java 11 HttpClient Bot") // add request header
            .header("Content-Type", "application/json")
            .build();

    HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

    return response;
  }

  private static HttpResponse<String> sendPostRegister(String input) throws Exception {

    // form parameters

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