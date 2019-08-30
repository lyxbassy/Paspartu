import com.google.googlejavaformat.java.FormatterException;
import resources.ResourceIndexBuilder;
import resources.ResourceUtility;
import tester.R;

import java.io.IOException;
import java.util.List;

public class UpdateTest {

    public static void main(String[] args) throws FormatterException, IOException {
        new ResourceIndexBuilder().withPathsRelative().withClass(R.class).build(ResourceUtility.getPathResources());

        List<String> strings = ResourceUtility.readLinesFromResources(R.Folder1.folder1_text1);
        for (String string : strings) {
            System.out.println(string);
        }

    }

}
