import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  private Socket socket;
  private final InputStream inputStream;
  private final OutputStream outputStream;

  public static final String ERROR_STRING = "Bad request";

  private static final String SECRET_MESSAGE = "SECRET MESSAGE!";

  String DEFAULT_RESPONSE_FORMAT = """
          HTTP/1.1 200 OK\r
          Server: YarServer/2009-09-09\r
          Content-Type: text/html\r
          Content-Length: %d\r
          Connection: close\r
          \r
          """;

  public Server(Socket socket) throws IOException {
    this.socket = socket;
    this.inputStream = socket.getInputStream();
    this.outputStream = socket.getOutputStream();
  }

  public static void start() {
    try {
      ServerSocket serverSocket = new ServerSocket(9991);
      while (true) {
        Socket socket = serverSocket.accept();
        Server server = new Server(socket);
        server.startServer();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void startServer() {
    try {
      readInput();
      socket.close();
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
  }

  private void readInput() throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
    while (true) {
      String request = reader.readLine();
      if (request == null || request.trim().length() == 0) {
        break;
      }
      if (request.startsWith("GET")) {
        String body = request.split(" ")[1];
        String response = process(body);
        writeResponse(response);
      }
    }
  }


  private void writeResponse(String responseData) throws IOException {
    String responseHeader = String.format(DEFAULT_RESPONSE_FORMAT, responseData.length());
    outputStream.write((responseHeader + responseData).getBytes());
    outputStream.flush();
  }

  public String process(String request) {
    if (request.equals("/secret")) {
      if (!isLogined())
        return ERROR_STRING;
      return SECRET_MESSAGE;
    } else if (request.startsWith("/register/"))
      return Authorizator.register(request.substring(10));
    else if (request.startsWith("/login/")) {
      if (isLogined())
        return ERROR_STRING;
      String ans = Authorizator.login(request.substring(7));
      if (!ans.equals(ERROR_STRING)) {
        addToLogined();
      }

      return ans;
    } else if (request.equals("/logout")) {
      if (!isLogined())
        return ERROR_STRING;
      removeLogined();

      return "You are logouted";

    } else return ERROR_STRING;

  }

  private boolean isLogined() {
    return Authorizator.loginedClients.contains(socket.getInetAddress());
  }

  private boolean addToLogined() {
    return Authorizator.loginedClients.add(socket.getInetAddress());
  }

  private boolean removeLogined() {
    return Authorizator.loginedClients.remove(socket.getInetAddress());
  }
}
