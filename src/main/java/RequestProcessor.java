import com.google.gson.Gson;
import java.util.concurrent.*;


interface AuthorizeAction{
  boolean execute(String data);
  String getLastMessage();
}

public class RequestProcessor {
  private static ConcurrentMap<String,String> dataBase = new ConcurrentHashMap<>();

  private static String message = "null";


  enum Actions{
    REGISTER, LOGIN, LOGOUT;
  }



  public static boolean processRequest(String actionType,String data){
    AuthorizeAction action;
    if(actionType.substring(1).equals(Actions.REGISTER.toString().toLowerCase())){
      action = new Registration();
      boolean response = action.execute(data);
      message = action.getLastMessage();
      return response;
    }

    return true; // temporary
  }

  public static String getLastMessage(){
    return message;
  }

  private static class Registration implements AuthorizeAction{

    private String message = "default";

    @Override
    public boolean execute(String data) {
      Info info = new Gson().fromJson(data, Info.class);

      if(dataBase.get(info.username)!=null){
        message = "You are already registered";
        return false;
      }

      dataBase.put(info.username,info.password);

      message = "You are registered";
      return true;
    }

    @Override
    public String getLastMessage() {
      return message;
    }
  }
}

class Info{
  String username;
  String password;
}





