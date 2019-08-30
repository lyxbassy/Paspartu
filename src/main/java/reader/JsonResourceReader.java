package reader;

import com.google.gson.Gson;
import resources.ResourceUtility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public abstract class JsonResourceReader<T> {

    public abstract String getResourceFilePath();

    protected T readConfig(Class<T> contentClass){
        List<T> configs = new ArrayList<>();
        Gson gson = new Gson();

        String jsonInString = null;
        try {
            jsonInString = ResourceUtility.readLinesFromResourcesAsString(getResourceFilePath());
            T config= gson.fromJson(jsonInString, contentClass);
            return config;

        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new RuntimeException("Could not read config for " + getResourceFilePath());
    }
}