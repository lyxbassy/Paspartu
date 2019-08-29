package resources;

import resources.model.Folder;

import java.io.FileWriter;
import java.util.regex.Pattern;

public class ttt {


    private boolean arePathsAbsolute;
    private boolean arePathRelative;
    private String packagePath;
    private String className;
    private Folder folder;


    public String build(){
        String appPath = System.getProperty("user.dir") ;
        String appendPackage = packagePath.replaceAll(Pattern.quote("."), "/") + "/";
        String fileName = appPath + "/src/main/java/" + appendPackage + className + ".java";

        String syntax = generateSyntax(folder);

        try {
            FileWriter aWriter = new FileWriter(fileName, false);
            aWriter.write(generateSyntax(folder));
            aWriter.flush();
            aWriter.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return syntax;
    }

    public ttt withFolder(Folder folder){
        this.folder = folder;
        return this;
    }

    public ttt withPackageName(String packagePath){
        this.packagePath = packagePath;
        return this;
    }

    public ttt withClassName(String className){
        this.className = className;
        return this;
    }

    public ttt withClass(Class clas){
        this.packagePath =  (clas.getPackage() + "").replaceAll("package", "").trim();
        this.className = clas.getSimpleName();
        return this;
    }

    public ttt withAbsolutePaths(){
        this.arePathRelative = false;
        this.arePathsAbsolute = true;
        return this;
    }

    public ttt withRelativePaths(){
        this.arePathRelative = true;
        this.arePathsAbsolute = false;
        return this;
    }

    private String generateSyntax(Folder folder){
        String rawSyntax = toClassSyntax(folder);
        rawSyntax = rawSyntax.substring(rawSyntax.indexOf("\n"));
        return  getPackagePath() + getPublicClassSyntax(className) + rawSyntax;
    }

    private String getPackagePath() {
        return "package " + packagePath + ";\n";
    }

    private String toClassSyntax(Folder folder){
        String classSyntax = getInnerClassSyntax(folder.getName());
        StringBuilder syntax = new StringBuilder();

        syntax.append(syntax + "\n");

        String staticField = "public static String %s = \"%s\";\n";
        String staticClassField = "public static %s %s;\n";

        for (String fileName : folder.getFiles()) {
            System.out.println(folder.getRelativePath());
            String relativePath = folder.getRelativePath();

            if (relativePath.contains("\\")){
                relativePath = relativePath.substring(1);
            }

            String field = String.format( staticField, fileName.split("\\.")[0], relativePath + "\\" +fileName);
            syntax.append(field.replace("\\", "/"));
        }

        for (Folder innerFolder : folder.getFolders()) {
            syntax.append("\n");
            syntax.append(toClassSyntax(innerFolder) + "\n");
        }

        return String.format(classSyntax, syntax.toString());
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
}
