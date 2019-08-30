package resources;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileUtility {

    public static String stripFileExtension (String str) {
        // Handle null case specially.
        if (str == null) return null;

        // Get position of last '.'.
        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.
        if (pos == -1) return str;

        // Otherwise return the string, up to the dot.
        return str.substring(0, pos);
    }

    private List<String> readFileToStringList(String pathname) throws IOException {
        File file = new File(pathname);
        List<String> lines = new ArrayList<>();

        try (Scanner scanner = new Scanner(file)) {
            while(scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            return lines;
        }
    }

    private List<String> readFileToStringList(File file) throws IOException {
        return readFileToStringList(file.getAbsoluteFile());
    }

    private String readFileToString(File file) throws IOException {
        return readFileToString(file.getAbsoluteFile());
    }

    private String readFileToString(String pathname) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (String line : readFileToStringList(pathname)) {
            sb.append(line +  System.lineSeparator());
        }
        return sb.toString();
    }


    public static void writeStringToFile(String filePath, String content) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
        writer.write(content);
        writer.close();
    }

}
