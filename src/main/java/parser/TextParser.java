package parser;

public class TextParser {

    public static String capitalizeString(String str){
        if (str.length() == 0){
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
