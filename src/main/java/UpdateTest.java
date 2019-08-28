import resources.FileFinder;

import java.io.File;
import java.util.List;

public class UpdateTest {

    public static void main(String[] args) {
        List<File> files = new FileFinder().find("./");
        for (File file : files) {
            System.out.println(file);
        }

    }

}
