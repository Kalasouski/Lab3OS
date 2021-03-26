package executor;

import com.google.gson.Gson;
import handler.JsonMessage;
import handler.RegInfo;

import java.util.Map;

public class Login implements AuthorizeAction {

    public Login() {
    }


    @Override
    public String[] execute(Map<RegInfo, Boolean> dataBase, String data) {
        RegInfo regInfo = new Gson().fromJson(data, RegInfo.class);

        if (!dataBase.containsKey(regInfo))
            return new String[]{"400", new Gson().toJson(new JsonMessage("Incorrect login or password"))};

        dataBase.put(regInfo, true);

        String secretInfo = Info.getInfo();

        return new String[]{"200", new Gson().toJson(new JsonMessage(secretInfo))};
    }
}
