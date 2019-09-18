package resources;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import parser.TextParser;
import resources.model.Folder;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.regex.Pattern;

public class ResourceIndexBuilder {

    private boolean pathsAbsolute = false;
    private boolean pathsRelative = true;
    private String classPackagePath;
    private String className;
    private File resourcePath;

    /**
     * Specify starting path of your resource folder.
     * Function will recursively search for all the files starting from that path and fill class R with the data
     *
     * Example usage:
     * new ResourceIndexBuilder().withClass(R.class).build("C:/Users/Comp/IdeaProjects/Paspartu/src/main/resources/");
     *
     * @param resourcePathString
     * @return
     * @throws FormatterException
     */
    public String build(String resourcePathString) throws FormatterException {
        this.resourcePath = new File(resourcePathString);
        if (!resourcePath.isDirectory()){
            throw new IllegalArgumentException("The path supplied to ResourceIndexBuilder is a file. Supply a folder path and run again");
        }

        String appPath = System.getProperty("user.dir") ;
        String appendPackage = classPackagePath.replaceAll(Pattern.quote("."), File.separator) + File.separator;
        String fileName = appPath + "/src/main/java/" + appendPackage + className + ".java";

        String classContent = generateClassContent();

        try {
            FileWriter aWriter = new FileWriter(fileName, false);
            aWriter.write(classContent);
            aWriter.flush();
            aWriter.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return classContent;
    }

    private String generateClassContent() throws FormatterException {
        String classContent =  getInnerClasser();

        classContent = classContent.substring(classContent.indexOf("\n")+1);


        classContent = getPublicClassSyntax() + classContent;

        System.out.println("---------");

        System.out.println(classContent);
        return new Formatter().formatSource(getPackagePath() + classContent);
    }

    private String getPackagePath() {
        return "package " + classPackagePath + ";\n";
    }

    private String getPublicClassSyntax() {
        return String.format("public class %s {\n", className);
    }

    private String getInnerClasser() {
        List<File> files = new FileFinder().find(resourcePath.getAbsolutePath());
        Folder folder = new Folder(resourcePath);
        folder.setFolder(new File(resourcePath.getAbsolutePath()));

        for (File f : files) {
            folder.add(f);
        }

        String s = getStaticClassSyntax(folder);
        System.out.println(s);
        return s;
    }

    private String getStaticClassSyntax(Folder folder){
        return getStaticClassSyntax(folder, "  ");
    }

    private String getStaticClassSyntax(Folder folder, String padding) {

        String all = "";

        for (Map.Entry<String, Folder> entry : folder.getFolders().entrySet()) {
            String allFields = "";
            String staticClassField = "public static String %s = \"%s\";\n";
            for (File file : entry.getValue().getFiles()) {
                String[] split = file.getName().split(Pattern.quote(File.separator));
                String fieldName = split[split.length-1];

                fieldName = FileUtility.stripFileExtension(fieldName);
                fieldName = fieldName.replaceAll("[^a-zA-z0-9$_]", "_");
                allFields += (padding + "   ");


                String fieldValue = "";
                if (pathsAbsolute){
                    fieldValue = file.getAbsolutePath();
                }else{
                    fieldValue = file.getAbsolutePath().substring(resourcePath.getAbsolutePath().length() + 1);
                }

                fieldValue = fieldValue.replaceAll(Pattern.quote("\\"), "/");
                allFields += String.format(staticClassField, fieldName,  fieldValue);
            }
            allFields += "\n";


            String sss = padding + "public static class %s {\n";
            String className = TextParser.capitalizeString(entry.getKey());
            sss = String.format(sss, className.replaceAll("[^a-zA-z0-9$_]", "_" ));
            sss +=  allFields;
            sss += "%s}\n";
            all += sss;

            System.out.println(folder);

            Folder value = entry.getValue();
            all = String.format(all, getStaticClassSyntax(value, padding + padding));
        }

        return all;
    }

    private String getInnerClassSyntax(String className){
        return "public static class "+ getClassNameUppercase(className) +" {%s}";
    }

    private String getClassNameUppercase(String className){
        return className.substring(0,1).toUpperCase() + className.substring(1);
    }

    public ResourceIndexBuilder withFileFinder(FileFinder fileFinder){
        return this;
    }

    public ResourceIndexBuilder withClass(Class clas){
        this.classPackagePath =  (clas.getPackage() + "").replaceAll("package", "").trim();
        this.className = clas.getSimpleName();
        return this;
    }

    public ResourceIndexBuilder withPackageName(String packagePath){
        this.classPackagePath = packagePath;
        return this;
    }

    public ResourceIndexBuilder withClassName(String className){
        this.className = className;
        return this;
    }

    public ResourceIndexBuilder withPathsAbsolute(){
        this.pathsAbsolute = true;
        this.pathsRelative = false;
        return this;
    }

    public ResourceIndexBuilder withPathsRelative(){
        this.pathsAbsolute = false;
        this.pathsRelative = true;
        return this;
    }
}
