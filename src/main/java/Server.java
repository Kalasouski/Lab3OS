

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

//  private Socket socket;
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
      try(Socket socket = serverSocket.accept()){
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        readInput();
      }
    }
  }




  private void readInput() throws IOException {
    try(BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))){


    String requestType = br.readLine();
    if(requestType==null)
      return;



    if(requestType.startsWith(String.valueOf(RequestMethods.GET))){

     // String actionType = requestType.split(" ")[1];


      //log out

    }
    else if(requestType.startsWith(String.valueOf(RequestMethods.POST))) {

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
      if (line != null) {
        char[] buf = new char[contentLength];  //<-- http body is here
        br.read(buf);
        System.out.println(buf);
      }

      writeResponse("Bro, you just posted cringe");

    }
    }

  }


  private void writeResponse(String responseData) throws IOException {
    String responseHeader = String.format(DEFAULT_RESPONSE_FORMAT, responseData.length());
    outputStream.write((responseHeader + responseData).getBytes());
    // outputStream.write(responseData.getBytes());
    outputStream.flush();
  }

  public String process(String request) {
   /* if (request.equals("/secret")) {
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

    } else return ERROR_STRING;*/
    return "okay";

  }

 /* private boolean isLogined() {
    return Authorizator.loginedClients.contains(socket.getInetAddress());
  }

  private boolean addToLogined() {
    return Authorizator.loginedClients.add(socket.getInetAddress());
  }

  private boolean removeLogined() {
    return Authorizator.loginedClients.remove(socket.getInetAddress());
  }*/
}
