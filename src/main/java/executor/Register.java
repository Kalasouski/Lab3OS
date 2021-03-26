package executor;

import com.google.gson.Gson;
import handler.JsonMessage;
import handler.RegInfo;

import java.util.Map;

public class Register implements AuthorizeAction {

    public Register() {
    }

    @Override
    public String[] execute(Map<RegInfo, Boolean> dataBase, String data) {
        RegInfo regInfo = new Gson().fromJson(data, RegInfo.class);
        if (dataBase.get(regInfo) != null)
            return new String[]{"400", new Gson().toJson(new JsonMessage("You are already registered"))};

        dataBase.put(regInfo, false);
        return new String[]{"200", new Gson().toJson(new JsonMessage("You have been registered." +
                " Now you can log in"))};
    }
}
