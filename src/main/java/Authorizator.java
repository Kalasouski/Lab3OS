import java.net.InetAddress;
import java.util.*;

public class Authorizator {
  static Map<String, String> dataBase = new HashMap<>();
  static Set<InetAddress> loginedClients = new HashSet<>();

  private static String[] check(String data) {
    String[] arr = data.split(":");
    if (arr.length != 2)
      return null;
    return arr;
  }

  public static String register(String request) {

    String[] arr = check(request);
    if (arr == null) {
      return Server.ERROR_STRING;
    }
    String login = arr[0], password = arr[1];
    if (dataBase.get(arr[0]) != null)
      return "you are registered";
    dataBase.put(login, password);
    return "congrats! now you can login";
  }

  public static String login(String request) {
    String[] arr = check(request);
    if (arr == null) {
      return Server.ERROR_STRING;
    }
    if (dataBase.get(arr[0]) == null || !dataBase.get(arr[0]).equals(arr[1]))
      return Server.ERROR_STRING;
    return "You are now logined. go to /secret to get message";
  }
}
