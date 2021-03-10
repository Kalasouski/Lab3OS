

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.*;



public class Server{
  private final ServerSocket serverSocket;
  ExecutorService pool = Executors.newCachedThreadPool();


  public Server(int port) throws IOException {
    serverSocket = new ServerSocket(port);
    startServer();
  }

  void startServer() throws IOException {
    while (true) {
      Socket socket = serverSocket.accept();

        MyThread req = new MyThread(socket);
        pool.execute(req);

      }
    }
}
