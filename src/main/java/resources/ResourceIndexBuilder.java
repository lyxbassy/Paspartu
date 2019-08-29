package resources;

import resources.model.Folder;

import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.regex.Pattern;

public class ResourceIndexBuilder {

    private boolean pathsAbsolute;
    private boolean pathsRelative;
    private String classPackagePath;
    private String className;

    public ResourceIndexBuilder(){

    }

    public String build(){
        String appPath = System.getProperty("user.dir") ;
        String appendPackage = classPackagePath.replaceAll(Pattern.quote("."), "/") + "/";
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

    private String generateClassContent(){
        return  getPackagePath() + getPublicClassSyntax();
    }

    private String getPackagePath() {
        return "package " + classPackagePath + ";\n";
    }

    private String getPublicClassSyntax() {
        return String.format("public class %s {\n%s\n}", className, getInnerClasser());
    }


    private String getResourcesPath(){
        String appPath = System.getProperty("user.dir") ;

        String fileName = appPath + "/src/main/resources/";
        return fileName;
    }

    private String getInnerClasser() {
        List<File> files = new FileFinder().find(getResourcesPath());
        Folder folder = new Folder();
        for (File f : files) {
            String filepath = f.getAbsolutePath().substring(getResourcesPath().length());
            folder.add(filepath);
        }


        String s = getStaticClassSyntax(folder);
        System.out.println(s);
        return s;
    }

    private String getStaticClassSyntax(Folder folder){
        return getStaticClassSyntax(folder, "");
    }

    private String getStaticClassSyntax(Folder folder, String padding) {


        String all = "";

        for (Map.Entry<String, Folder> entry : folder.getFolders().entrySet()) {
            String allFields = "";
            String staticClassField = "public static String %s = \"%s\";\n";
            for (String file : entry.getValue().getFiles()) {
                String[] split = file.split(File.separator);
                String fieldName = split[split.length-1];
                fieldName = fieldName.replaceAll("\\..*", "");
                allFields += (padding + padding);
                allFields += String.format(staticClassField, fieldName, file);
            }
            allFields += "\n";


            String sss = padding + "public static class %s {\n";
            sss = String.format(sss, entry.getKey());
            sss +=  allFields;
            sss += "%s}\n";
            all += sss;

            System.out.println(folder);




            Folder value = entry.getValue();
            all = String.format(all, getStaticClassSyntax(value, padding + "  "));
        }

        return all;
    }

    private String getPublicClassSyntax(String className){
        return String.format("public class %s {", className);
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

    public ResourceIndexBuilder withPackageName(String packagePath){
        this.classPackagePath = packagePath;
        return this;
    }

    public ResourceIndexBuilder withClassName(String className){
        this.className = className;
        return this;
    }

    public ResourceIndexBuilder withClass(Class clas){
        this.classPackagePath =  (clas.getPackage() + "").replaceAll("package", "").trim();
        this.className = clas.getSimpleName();
        return this;
    }

    class Folder {
        Map<String, Folder> folders = new HashMap<>();
        List<String> files = new ArrayList<>();

        void add(String filepath){
            String[] split = filepath.split(File.separator);
            Folder folder = new Folder();

            Folder folderToAdd = this;
            for (int i = 0; i < split.length - 1; i++) {
                String folderName = split[i];
                if (!folderToAdd.getFolders().containsKey(folderName)){
                    folderToAdd.getFolders().put(folderName, new Folder());
                }
                folderToAdd = folderToAdd.getFolders().get(folderName);
            }

            folderToAdd.getFiles().add(filepath);
            System.out.println(Arrays.toString(split));
        }

        public Map<String, Folder> getFolders() {
            return folders;
        }

        public void setFolders(Map<String, Folder> folders) {
            this.folders = folders;
        }

        public List<String> getFiles() {
            return files;
        }

        public void setFiles(List<String> files) {
            this.files = files;
        }

        @Override
        public String toString() {
            return "Folder{" +
                    "folders=" + folders +
                    ", files=" + files +
                    '}';
        }
    }
}
