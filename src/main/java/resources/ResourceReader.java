package resources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ResourceReader {
    public static String getPathMain(){
        String appPath = System.getProperty("user.dir") ;
        return appPath + "/src/main/";
    }
    public static String getPathResources(){
        return getPathMain() + "resources/";
    }

    public static List<String> readLinesFromResources(String resourceFilePath) throws IOException {
        List<String> allLines = new ArrayList<>();

        InputStream resourceAsStream = ResourceReader.class.getClassLoader().getResourceAsStream(resourceFilePath);
        if (resourceAsStream != null){
            BufferedReader br = new BufferedReader(new InputStreamReader(resourceAsStream));
            String line;

            while ((line = br.readLine()) != null) {
                allLines.add(line);
            }
        } else{
            throw new IllegalArgumentException(String.format("Resource %s wasn't found", resourceFilePath));
        }
        return allLines;
    }

    public static String readLinesFromResourcesAsString(String fileName) throws IOException {
        List<String> allLines = readLinesFromResources(fileName);
        StringBuilder sb = new StringBuilder();

        for (String line : allLines) {
            sb.append(line).append("\n");
        }

        return sb.toString();
    }

}
