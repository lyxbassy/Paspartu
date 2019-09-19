package utils;

import java.util.Collection;

public class CollectionUtility {
    public static String collectionToStringSeperatedWithCharacter(Collection collection, String character){
        StringBuilder sb = new StringBuilder();
        for (Object o : collection) {
            sb.append(o + character);
        }
        String content = sb.toString();
        content = content.substring(0, content.lastIndexOf(character));
        return content;
    }
}
