

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

  private final ServerSocket serverSocket;
  private InputStream inputStream;
  private OutputStream outputStream;

  public static final String ERROR_STRING = "Bad request";

  String DEFAULT_RESPONSE_FORMAT = """
          HTTP/1.1 200 OK\r
          Server: YarServer/2009-09-09\r
          Content-Type: text/html\r
          Content-Length: %d\r
          Connection: close\r
          \r
          """;

  public Server(int port) throws IOException {
    serverSocket = new ServerSocket(port);
    start();
  }

  private void start() throws IOException {
    while (true) {
      try (Socket socket = serverSocket.accept()) {
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        readInput();
      }
    }
  }

  private void readInput() throws IOException {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
      String requestType = br.readLine();
      if (requestType == null)
        return;

      AuthorizeAction action;
      if (requestType.startsWith(RequestMethods.GET.toString())) {
        // String actionType = requestType.split(" ")[1];
        //log out
      } else if (requestType.startsWith(RequestMethods.POST.toString())) {

        String actionType = requestType.split(" ")[1];
        if (actionType.length() <= 1) {
          writeResponse("Error : 400"); // code error
          return;
        }
        boolean headersFinished = false;
        int contentLength = -1;
        String line = "";

        while (!headersFinished) {
          line = br.readLine();
          if (line == null)
            break;
          headersFinished = line.isEmpty();

          if (line.startsWith("Content-Length:")) {
            String cl = line.substring("Content-Length:".length()).trim();
            contentLength = Integer.parseInt(cl);
          }
        }

        // validate contentLength value
        char[] buf = new char[contentLength];  //<-- http body is here
        br.read(buf);
        System.out.println(buf);
        RequestProcessor.processRequest(actionType,new String(buf));
        writeResponse(RequestProcessor.getLastMessage());

      }
    }
  }

  private void writeResponse(String responseData) throws IOException {
    String responseHeader = String.format(DEFAULT_RESPONSE_FORMAT, responseData.length());
    outputStream.write((responseHeader + responseData).getBytes());
    outputStream.flush();
  }


}
