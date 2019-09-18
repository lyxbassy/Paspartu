import com.google.googlejavaformat.java.FormatterException;
import resources.ResourceIndexBuilder;
import resources.ResourceUtility;
import tester.R;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class UpdateTest {

    public static void main(String[] args) throws FormatterException, IOException, URISyntaxException {
        new ResourceIndexBuilder().withPathsRelative().withClass(R.class).build(ResourceUtility.getPathResources());


        System.out.println(ResourceUtility.getPathJar());
    }

}
