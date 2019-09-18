package tester;

import com.google.googlejavaformat.java.FormatterException;
import resources.ResourceIndexBuilder;

public class Init {

    public static void main(String[] args) throws FormatterException {

        new ResourceIndexBuilder().withClass(R.class).build("C:/Users/Comp/IdeaProjects/Paspartu/src/main/resources/");
    }
}
