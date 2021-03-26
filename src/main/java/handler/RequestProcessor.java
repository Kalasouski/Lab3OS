package handler;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.Gson;
import executor.AuthorizeAction;

public class RequestProcessor {

    private static final Map<RegInfo, Boolean> dataBase = new ConcurrentHashMap<>();

    public static String[] processRequest(String actionType, String body) {

        String className = actionType.substring(1, 2).toUpperCase() + actionType.substring(2);
        AuthorizeAction action = null;

        try {
            Class<?> clazz = Class.forName("executor."+className);
            Constructor<?> ctor = clazz.getConstructor();
            action = (AuthorizeAction) ctor.newInstance();
        } catch (Exception e) {
            return new String[]{"400", new Gson().toJson(new JsonMessage("Incorrect request type"))};
        }

        return action.execute(dataBase, body);

    }
}



