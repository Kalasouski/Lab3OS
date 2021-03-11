import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.Assert.assertEquals;

class ServerTest {

  @BeforeEach
  public void setUp() throws IOException {
    Server server = new Server(9991);
    server.serverShutDown();
  }

  @Test
  public void testServer() {

    System.out.println("dfvg");
  }

}