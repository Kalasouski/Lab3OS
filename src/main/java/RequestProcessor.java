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
    if(actionType.equals(Actions.REGISTER.toString())){
      action = new Registration();
      if(action.execute(data))
        return true;
      message = action.getLastMessage();
      return false;
    }

    return true; // temporary
  }

  public static String getLastMessage(){
    return message;
  }

  private static class Registration implements AuthorizeAction{

    @Override
    public boolean execute(String data) {

      return true;
    }

    @Override
    public String getLastMessage() {
      return null;
    }
  }


}





