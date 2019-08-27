package resources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileFinder {

    private CheckCondition filePathCondition = null;
    private CheckCondition fileNameCondition = name -> true;
    private CheckCondition ignoreCondition = name -> false;

    private List<String> filePathContains = new ArrayList<>();

    private List<String> fileNameStartWith = new ArrayList<>();
    private List<String> fileNameEndWith = new ArrayList<>();
    private List<String> fileNameContains = new ArrayList<>();
    private List<String> fileNameIs = new ArrayList<>();

    private List<String> ignoreFileNameThatStartWith = new ArrayList<>();
    private List<String> ignoreFileNameThatEndWith = new ArrayList<>();
    private List<String> ignoreFileNameThatContains = new ArrayList<>();
    private List<String> ignoreFileNameThatIs = new ArrayList<>();

    private boolean isRecursive = true;

    List<String> ignoreFileNames = new ArrayList<>();

    public FileFinder(){
        this.ignoreCondition = name -> ignoreFileNames.contains(name);
    }

    public FileFinder filePathContains(final String path){
        filePathContains.add(path);
        return this;
    }

    public FileFinder fileNameStartWith(final String fileName){
        fileNameStartWith.add(fileName);

        return this;
    }

    public FileFinder fileNameEndWith(final String fileName){
        fileNameEndWith.add(fileName);

        return this;
    }

    public FileFinder fileNameContains(final String fileName){
        fileNameContains.add(fileName);

        return this;
    }

    public FileFinder fileNameIs(List<String> rewrites) {
        for (String file : rewrites) {
            fileNameEndWith.add(file);
        }
        return this;
    }

    public FileFinder fileNameIs(final String... fileName){
        for (String file : fileName) {
            fileNameEndWith.add(file);
        }

        return this;
    }

    public FileFinder isRecursive(boolean isRecursive){
        this.isRecursive = isRecursive;
        return this;
    }

    public FileFinder ignoreFilesWithName(String fileToIgnore){
        ignoreFileNames.add(fileToIgnore);
        return this;
    }

    public List<File> findAll(String path){

        buildCheckFileNameCondition();
        buildCheckFilePathCondition();


        List<File> all = find(path);
        List<File> toRemove = new ArrayList<>();
        List<File> toReturn = new ArrayList<>();

        for (File file : all) {
            if (fileNameCondition.checkCondition(file.getName()) && filePathCondition.checkCondition(file.getAbsolutePath())){
                toReturn.add(file);
            }
        }

        for (File file : toReturn) {
            String fileName = file.getName();
            if (ignoreCondition.checkCondition(fileName)){
                toRemove.add(file);
            }
        }
        toReturn.removeAll(toRemove);

        return toReturn;
    }

    private void buildCheckFileNameCondition() {
        final CheckCondition checkFileNameStartWith = name -> {
            if (fileNameStartWith.size() == 0){
                return true;
            }
            for (String start : fileNameStartWith) {
                if (name.startsWith(start)){
                    return true;
                }
            }
            return false;
        };

        final CheckCondition checkFileNameEndWith = name -> {
            if (fileNameEndWith.size() == 0){
                return true;
            }
            for (String end : fileNameEndWith) {
                if (name.endsWith(end)){
                    return true;
                }
            }
            return false;
        };

        final CheckCondition checkFileNameIs = name -> {
            if (fileNameIs.size() == 0){
                return true;
            }
            for (String fileName : fileNameIs) {
                if (name.equals(fileName)){
                    return true;
                }
            }
            return false;
        };

        this.fileNameCondition = name -> {
            boolean nameEnd = checkFileNameEndWith.checkCondition(name);
            boolean nameIs =checkFileNameIs.checkCondition(name);
            boolean nameStart =checkFileNameStartWith.checkCondition(name);
            return nameEnd && nameIs && nameStart;
        };
    }

    private void buildCheckFilePathCondition() {
        if (filePathContains.size() == 0){
            filePathCondition=name -> true;
        } else{
            filePathCondition = name -> {
                for (String containSubstring : filePathContains) {
                    if (name.contains(containSubstring)){
                        return true;
                    }
                }
                return false;
            };
        }
    }

    private List<File> find(String path){
        List<File> list = new ArrayList<>();

        File file = new File(path);
        if (file.exists()){
            File[] files = file.listFiles();
            for (File f : Objects.requireNonNull(files)) {
                if (f.isDirectory()){
                    if (isRecursive){
                        list.addAll(find(f.getAbsolutePath()));
                    }
                }else {
                    list.add(f);
                }
            }

        }

        return list;
    }



    private interface CheckCondition{
        boolean checkCondition(String name);
    }
}