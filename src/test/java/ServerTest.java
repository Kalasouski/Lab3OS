import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;


class ServerTest {

  @BeforeEach
  public void setUp() throws IOException {
    Server server = new Server(9991);
  }

  @Test
  public void testServer() {

    System.out.println("dfvg");
  }

}