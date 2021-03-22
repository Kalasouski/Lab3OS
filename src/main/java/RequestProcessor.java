import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.*;
import java.util.concurrent.*;


interface AuthorizeAction {
    boolean execute(String data);

    ReplyInfo getLastMessage();
}

public class RequestProcessor {
    private static final Map<String, String> dataBase = new ConcurrentHashMap<>();

    private static final Set<String> loginUsers = new ConcurrentSkipListSet<>();

    private static ReplyInfo message = null;

    enum Actions {
        REGISTER, LOGIN, LOGOUT
    }

    public static boolean processRequest(String actionType) {

        if (actionType.substring(1).startsWith(Actions.LOGOUT.toString().toLowerCase() + '?')) {
            AuthorizeAction action = new Logout();
            boolean response = action.execute(actionType.substring(actionType.indexOf('?') + 1));
            message = action.getLastMessage();
            return response;
        }

        message = new ReplyInfo(400,
                new Gson().toJson(new JsonMessage("Incorrect request")));
        return false;
    }

    public static boolean processRequest(String actionType, String data) {
        AuthorizeAction action;
        if (actionType.substring(1).equals(Actions.REGISTER.toString().toLowerCase()))
            action = new Registration();
        else if (actionType.substring(1).equals(Actions.LOGIN.toString().toLowerCase()))
            action = new Login();
        else {
            message = new ReplyInfo(400,
                    new Gson().toJson(new JsonMessage("Incorrect request")));
            return false;
        }


        boolean response = action.execute(data);
        message = action.getLastMessage();
        return response;
    }

    public static ReplyInfo getLastMessage() {
        return message;
    }

    private static class Login implements AuthorizeAction {

        private ReplyInfo message;

        @Override
        public boolean execute(String data) {

            RegInfo regInfo;
            try {
                regInfo = new Gson().fromJson(data, RegInfo.class);
                if (regInfo == null||regInfo.username == null||regInfo.password == null)
                    throw new JsonSyntaxException("");
            } catch (JsonSyntaxException e) {
                message = new ReplyInfo(400,
                        new Gson().toJson(new JsonMessage("Error when parsing Json")));
                return false;
            }

            if (loginUsers.contains(regInfo.username)) {
                message = new ReplyInfo(400,
                        new Gson().toJson(new JsonMessage("You have already entered")));
                return false;
            }
            if (dataBase.get(regInfo.username) == null) {
                message = new ReplyInfo(400,
                        new Gson().toJson(new JsonMessage("You are not registered")));
                return false;
            }
            if (!dataBase.get(regInfo.username).equals(regInfo.password)) {
                message = new ReplyInfo(400,
                        new Gson().toJson(new JsonMessage("Incorrect password")));
                return false;
            }

            loginUsers.add(regInfo.username);
            message = new ReplyInfo(200,
                    new Gson().toJson(new JsonMessage("Correct password")));
            return true;
        }

        @Override
        public ReplyInfo getLastMessage() {
            return message;
        }
    }

    private static class Registration implements AuthorizeAction {

        private ReplyInfo message;

        @Override
        public boolean execute(String data) {
            RegInfo regInfo = new Gson().fromJson(data, RegInfo.class);

            if (dataBase.get(regInfo.username) != null) {
                message = new ReplyInfo(400,
                        new Gson().toJson(new JsonMessage("You are already registered")));
                return false;
            }

            dataBase.put(regInfo.username, regInfo.password);

            message = new ReplyInfo(200, new Gson().toJson(new JsonMessage("You are registered")));
            return true;
        }

        @Override
        public ReplyInfo getLastMessage() {
            return message;
        }
    }

    private static class Logout implements AuthorizeAction {

        private ReplyInfo message;

        @Override
        public boolean execute(String userName) {

            if (!loginUsers.remove(userName)) {
                message = new ReplyInfo(400,
                        new Gson().toJson(
                                new JsonMessage("The account with your username is not" +
                                        " in system or not registered")));
                return false;
            } else
                message = new ReplyInfo(200,
                        new Gson().toJson(new JsonMessage("You have logged out")));
            return true;
        }

        @Override
        public ReplyInfo getLastMessage() {
            return message;
        }
    }

}

class RegInfo {
    String username, password;
}







