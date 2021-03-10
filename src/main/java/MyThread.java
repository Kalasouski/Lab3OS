import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class MyThread implements Runnable {

  final Socket socket;
  private final InputStream inputStream;
  private final OutputStream outputStream;

  static int i = 0;

  String DEFAULT_RESPONSE_FORMAT = """
          HTTP/1.1 200 OK\r
          Server: YarServer/2009-09-09\r
          Content-Type: text/html\r
          Content-Length: %d\r
          Connection: close\r
          \r
          """;

  public MyThread(Socket socket) throws IOException {
    this.socket = socket;

    inputStream = socket.getInputStream();
    outputStream = socket.getOutputStream();
  }



  public void run(){
    readMessage();
    try {
      socket.close();
    } catch (IOException e) {
      e.printStackTrace();
    }


  }

  private void readMessage() {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
      String requestType = br.readLine();
      if (requestType == null)
        return;

      AuthorizeAction action;
      if (requestType.startsWith(RequestMethods.GET.toString())) {
        String actionType = requestType.split(" ")[1];
        RequestProcessor.processRequest(actionType);
        writeResponse(RequestProcessor.getLastMessage());


      } else if (requestType.startsWith(RequestMethods.POST.toString())) {

        String actionType = requestType.split(" ")[1];

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
        RequestProcessor.processRequest(actionType,new String(buf));
        writeResponse(RequestProcessor.getLastMessage());

      }
      else{
        writeResponse("Incorrect request type");
      }
    }
    catch(IOException e){System.out.println(e);}
  }

  private void writeResponse(String responseData) throws IOException {
    String responseHeader = String.format(DEFAULT_RESPONSE_FORMAT, responseData.length());
    outputStream.write((responseHeader + responseData).getBytes());
    outputStream.flush();
  }
}
