package executor;

import com.google.gson.Gson;
import handler.JsonMessage;
import handler.RegInfo;

import java.util.Map;

public interface AuthorizeAction {
    String[] execute(Map<RegInfo, Boolean> dataBase, String data);
}

