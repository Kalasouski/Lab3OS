

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.HashSet;
import java.util.Set;
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
      MyThread req = new MyThread(serverSocket.accept());
      pool.execute(req);
      }
    }
}
