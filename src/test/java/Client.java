import java.net.URI;
import java.net.http.*;

public class Client {
    static String requestData ="{\"username\" : \"user\", \"password\" : \"69\"}";

    private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();

    public static void main(String[] args) throws Exception {

        Client obj = new Client();

        try {

            System.out.println("Testing 1 - Send Http POST request: register");
            assert(printResponse(obj.sendPostRegister(requestData)) == 200);

            System.out.println("Testing 2 - Send Http POST request: login\n");
            assert (printResponse(obj.sendPostLogin(requestData))==200);

            System.out.println("Testing 3 - Send Http GET request: logout\n");
            assert(printResponse(obj.sendGetLogout())==200);

            System.out.println("Testing 4 - Send Http POST request: register\nWhen registered\n");
            assert (printResponse(obj.sendPostRegister(requestData))==400);

            System.out.println("Testing 5 - Send Http GET request: logout\nWhen not logged in\n");
            assert (printResponse(obj.sendGetLogout())==400);
        }catch (AssertionError e) {
            System.out.println("Not OK: Assertion error");
        }
    }

    public HttpResponse<String> sendGetLogout() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:9991/logout?user"))
                .setHeader("User-Agent", "Java 11 HttpClient Bot")
                .build();


        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }

    public static int printResponse(HttpResponse<String> response){

        // print response
        System.out.println(response.statusCode()+"\n"+response.body());
        return response.statusCode();
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