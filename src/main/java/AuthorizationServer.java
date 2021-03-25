import java.io.*;
import java.net.ServerSocket;

public class AuthorizationServer {
    private final ServerSocket serverSocket;

    public AuthorizationServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void startServer() throws IOException {
        while(true)
            new ClientHandler(serverSocket.accept()).readMessage();
    }
}
