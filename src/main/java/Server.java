

import java.io.*;
import java.net.ServerSocket;
import java.util.concurrent.*;

public class Server {
    private final ServerSocket serverSocket;
    private final ExecutorService pool = Executors.newFixedThreadPool(10);

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        startServer();
    }

    void startServer() throws IOException {
        while (true)
            pool.execute(new ThreadPoolExecutor(serverSocket.accept()));
    }
}
