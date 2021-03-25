import com.google.gson.Gson;
import java.util.Map;

interface AuthorizeAction {
    String[] execute(Map<RegInfo, Boolean> dataBase, String data);
}

class Register implements AuthorizeAction {

    public Register(){}

    @Override
    public String[] execute(Map<RegInfo, Boolean> dataBase, String data) {
        RegInfo regInfo = new Gson().fromJson(data, RegInfo.class);
        if (dataBase.get(regInfo) != null)
            return new String[]{"400", new Gson().toJson(new JsonMessage("You are already registered"))};

        dataBase.put(regInfo,false);
        return new String[]{"200", new Gson().toJson(new JsonMessage("You have been registered." +
                " Now you can log in"))};
    }
}

class Login implements AuthorizeAction{

    public Login(){}


    @Override
    public String[] execute(Map<RegInfo, Boolean> dataBase, String data) {
        RegInfo regInfo = new Gson().fromJson(data, RegInfo.class);

        if(!dataBase.containsKey(regInfo))
            return new String[]{"400", new Gson().toJson(new JsonMessage("Incorrect login or password"))};
        

        dataBase.put(regInfo,true);

        String secretInfo = Info.getInfo();

        return new String[]{"200", new Gson().toJson(new JsonMessage(secretInfo))};

    }
}

