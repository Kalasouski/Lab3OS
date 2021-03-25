import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class AuthorizationServer {
    private final ServerSocket serverSocket;
    private final List<Socket> activeClients = new ArrayList<>();

    public AuthorizationServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void startServer() throws ExecutionException, InterruptedException {
        final ExecutorService clientPool = Executors.newCachedThreadPool();

        while(!serverSocket.isClosed()){
            Future<Socket> future = clientPool.submit(() ->{
                Socket socket = serverSocket.accept();
                System.out.println(socket);
                new ClientHandler(socket).readMessage();

                return socket;
            });
            activeClients.add(future.get());
        }


    }

    public void stopServer() throws IOException {
        serverSocket.close();
        for(Socket socket : activeClients)
            socket.close();

    }
}
