import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.*;
import java.util.concurrent.*;


interface AuthorizeAction{
  boolean execute(String data);
  String getLastMessage();
}

public class RequestProcessor {
  private static final Map<String,String> dataBase = new ConcurrentHashMap<>();

  private static final Set<String> loginUsers = new ConcurrentSkipListSet<>();

  private static String message = "null";

  enum Actions{
    REGISTER, LOGIN, LOGOUT
  }

  public static boolean processRequest(String actionType){

    if(actionType.substring(1).startsWith(Actions.LOGOUT.toString().toLowerCase()+'?')){
      AuthorizeAction action = new Logout();
      boolean response = action.execute(actionType.substring(actionType.indexOf('?')+1));
      message = action.getLastMessage();
      return response;
    }


    message = "Incorrect input";
    return false;
  }

  public static boolean processRequest(String actionType,String data){
    AuthorizeAction action;
    if(actionType.substring(1).equals(Actions.REGISTER.toString().toLowerCase()))
      action = new Registration();
    else if(actionType.substring(1).equals(Actions.LOGIN.toString().toLowerCase()))
      action = new Login();
    else{
      message = "Incorrect";
      return false;
    }


      boolean response = action.execute(data);
      message = action.getLastMessage();
      return response;
  }

  public static String getLastMessage(){
    return message;
  }

  private static class Login implements AuthorizeAction{

    private String message;

    @Override
    public boolean execute(String data) {

      Info info = null;
      try{
         info = new Gson().fromJson(data, Info.class);
        if(info==null || info.username==null || info.password == null)
          throw new JsonSyntaxException("");
      }
      catch(JsonSyntaxException e){
        message = "Error when parsing Json";
        return false;
      }




      if(loginUsers.contains(info.username)){
        message = "You are already entered";
        return false;
      }
      if(dataBase.get(info.username)==null){
        message = "You are not registered";
        return false;
      }
      if(!dataBase.get(info.username).equals(info.password)){
        message = "Incorrect password";
        return false;
      }

      loginUsers.add(info.username);
      message = "Correct password";



      return true;
    }

    @Override
    public String getLastMessage() {
      return message;
    }
  }

  private static class Registration implements AuthorizeAction{

    private String message;

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
  private static class Logout implements AuthorizeAction{

    private String message;

    @Override
    public boolean execute(String userName) {

      if(!loginUsers.remove(userName)){
        message = "The account with your username is not in system or not registered";
        return false;
      }
      else
        message = "You have logged out";
      return true;
    }

    @Override
    public String getLastMessage() {
      return message;
    }
  }

}

class Info{ String username, password;}





