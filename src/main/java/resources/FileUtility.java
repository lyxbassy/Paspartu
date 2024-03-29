package resources;

import java.io.*;
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

    private static String getParentFolder(String path) {
        File file = new File(path);
        if (file.isDirectory()){
            return file.getAbsolutePath() + "/";
        }
        return file.getParent() + "/";
    }

    private static String getParentFolder(File file) {
        if (file.isDirectory()){
            return file.getAbsolutePath() + "/";
        }
        return file.getParent() + "/";
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

    private static void writeByteToFile(byte[] b, String path) throws IOException {
        File file = new File(path);
        file.getParentFile().mkdir();

        FileOutputStream fos = new FileOutputStream(path);
        fos.write(b);
        fos.close();
    }

}
