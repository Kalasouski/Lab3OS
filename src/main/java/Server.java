

import java.io.*;
import java.net.ServerSocket;
import java.util.concurrent.*;

public class Server{
  private final ServerSocket serverSocket;

  ExecutorService pool = Executors.newCachedThreadPool();

  boolean isOn = true;

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

  void serverShutDown(){
    isOn = false;
  }
}
