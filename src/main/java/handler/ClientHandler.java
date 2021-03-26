package handler;

import com.google.gson.Gson;

import java.io.*;
import java.net.Socket;

public class ClientHandler {

    private final Socket socket;
    private final InputStream inputStream;
    private final OutputStream outputStream;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
    }

    public void readMessage() {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            String firstLine = br.readLine();
            if (firstLine == null)
                return;
            if (firstLine.startsWith("POST"))
                POSTRequestHandler(firstLine, br);
            else
                writeResponse(new String[]{"400", new Gson().toJson(new JsonMessage("Incorrect request type"))});


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void POSTRequestHandler(String message, BufferedReader br) throws IOException {
        String actionType = message.split(" ")[1];

        boolean headersFinished = false;
        int contentLength = -1;

        while (!headersFinished) {
            String line = br.readLine();
            if (line == null)
                break;
            headersFinished = line.isEmpty();

            if (line.startsWith("Content-Length:")) {
                String cl = line.substring("Content-Length:".length()).trim();
                contentLength = Integer.parseInt(cl);
            }
        }
        char[] buf = new char[contentLength];
        if (br.read(buf) == -1) {
            //error
        }
        String body = new String(buf);

        String[] resultMessage = RequestProcessor.processRequest(actionType, body);

        writeResponse(resultMessage);
    }


    private void writeResponse(String[] resultMessage) throws IOException {
        String DEFAULT_RESPONSE_FORMAT = """ 
                HTTP/1.1 %s     
                Server: YarServer/2009-09-09
                Content-Type: text/html
                Content-Length: %s
                Connection: close
                \r                         
                """;

        String responseHeader = String.format(DEFAULT_RESPONSE_FORMAT, resultMessage[0], resultMessage[1].length());
        outputStream.write((responseHeader + resultMessage[1]).getBytes());
        outputStream.flush();
    }

}
