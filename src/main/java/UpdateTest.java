import resources.FileFinder;
import resources.ResourceIndexBuilder;
import tester.R;

import java.io.File;
import java.util.List;

public class UpdateTest {

    public static void main(String[] args) {
        new ResourceIndexBuilder().withClass(R.class).build();

    }

}
