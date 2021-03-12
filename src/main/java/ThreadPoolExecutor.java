import com.google.gson.Gson;
import java.io.*;
import java.net.Socket;


public class ThreadPoolExecutor implements Runnable {

  private final Socket socket;
  private final InputStream inputStream;
  private final OutputStream outputStream;

  public ThreadPoolExecutor(Socket socket) throws IOException {
    this.socket = socket;
    this.inputStream = socket.getInputStream();
    this.outputStream = socket.getOutputStream();
  }

  @Override
  public void run(){
    readMessage();
  }

  private void readMessage() {
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
      String requestType = br.readLine();
      if (requestType == null)
        return;

      if (requestType.startsWith(RequestMethods.GET.toString())) {
        String actionType = requestType.split(" ")[1];
        RequestProcessor.processRequest(actionType);
        ReplyInfo reply = RequestProcessor.getLastMessage();
        writeResponse(reply);


      } else if (requestType.startsWith(RequestMethods.POST.toString())) {

        String actionType = requestType.split(" ")[1];

        boolean headersFinished = false;
        int contentLength = -1;
        String line;

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

        char[] buf = new char[contentLength];
        br.read(buf);
        RequestProcessor.processRequest(actionType,new String(buf));
        ReplyInfo reply = RequestProcessor.getLastMessage();
        writeResponse(reply);

      }
      else{
        ReplyInfo reply = new ReplyInfo("400 Bad Request",new Gson().toJson(
                new JsonMessage("Incorrect request type")));
        writeResponse(reply);
      }
      socket.close();
    }
    catch(IOException e){e.printStackTrace();}
  }

  private void writeResponse(ReplyInfo reply) throws IOException {

    String DEFAULT_RESPONSE_FORMAT = """ 
            HTTP/1.1 %s     
            Server: YarServer/2009-09-09
            Content-Type: text/html
            Content-Length: %d
            Connection: close
            \r                         
            """;

    String responseHeader = String.format(DEFAULT_RESPONSE_FORMAT,reply.responseCode, reply.message.length());
    outputStream.write((responseHeader + reply.message).getBytes());
    outputStream.flush();
  }
}

class ReplyInfo {
  final String responseCode;
  final String message;
  ReplyInfo(String responseCode, String message){this.responseCode = responseCode;this.message = message;}
}

class JsonMessage{
  final String reply;
  JsonMessage(String reply){this.reply = reply;}
}
