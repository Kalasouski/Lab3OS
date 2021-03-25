import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;


public class RequestProcessor {


    private static final Map<RegInfo, Boolean> dataBase = new ConcurrentHashMap<>();

    public static String[] processRequest(String actionType,String body){

        String className = actionType.substring(1,2).toUpperCase()+actionType.substring(2);
        AuthorizeAction action = null;

        try {
            Class<?> clazz = Class.forName(className);
            Constructor<?> ctor = clazz.getConstructor();
            action = (AuthorizeAction) ctor.newInstance();
        } catch (Exception e) {
            return new String[]{"400", new Gson().toJson(new JsonMessage("Incorrect request type"))};
        }

        return action.execute(dataBase,body);

    }
}

class JsonMessage {
    final String reply;
    JsonMessage(String reply) {
        this.reply = reply;
    }
}

class RegInfo {
    String username, password;

    @Override
    public boolean equals(Object obj){
        RegInfo regInfo = (RegInfo) obj;

        return this.username.equals(regInfo.username) && this.password.equals(regInfo.password);
    }

    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((username == null) ? 0 : username.hashCode());
        return result;
    }
}



