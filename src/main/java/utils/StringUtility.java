package utils;

public class StringUtility {
    public static String capitalizeString(String str){
        String firstChar = "";
        String restOfWord = "";

        if (str.length() > 0){
            firstChar = str.toUpperCase().charAt(0) + "";
        }
        if (str.length() > 0){
            restOfWord = str.substring(1).toLowerCase();
        }
        return firstChar + restOfWord;
    }

    public static String capitalizeWordsInSentence(String str){
        String output = "";
        String[] split = str.split("\\s+");
        for (String s : split) {
            output += capitalizeString(s);
            output += " ";
        }

        if (split.length <= 1){
            output = str;
        }

        output = output.trim();
        return output;
    }

    public static boolean isNumeric(String strNum) {
        return strNum.matches("-?\\d+(\\.\\d+)?");
    }

    public static String padRight(String str, int padCount, String c) {
        String paddedStr = "";
        for (int i = 0; i < padCount; i++) {
            paddedStr += c;
        }
        paddedStr += str;
        return paddedStr;
    }

    public static String padLeft(String str, int padCount, String c) {
        String paddedStr = "";
        String padded = "";
        for (int i = 0; i < padCount; i++) {
            padded += c;
        }
        paddedStr = str + padded;
        return paddedStr;
    }
}
