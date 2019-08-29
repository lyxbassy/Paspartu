package resources;

import java.io.File;
import java.util.*;

public class FileFinder {

    private boolean isSearchRecursive = true;
    private CheckCondition selectFileCondition;
    private CheckCondition ignoreFileCondition;
    private CheckCondition ignoreFoldenCondition;

    /*
     * Approve file settings
     ********************************************************/
    private List<String> fileNameStartsWith = new ArrayList<>();
    private List<String> fileNameEndsWith = new ArrayList<>();
    private List<String> fileNameContains = new ArrayList<>();
    private List<String> fileNameIs = new ArrayList<>();

    /*
     * Ignore file settings
     ********************************************************/
    private List<String> ignoreFileNameThatStartWith = new ArrayList<>();
    private List<String> ignoreFileNameThatEndWith = new ArrayList<>();
    private List<String> ignoreFileNameThatContains = new ArrayList<>();
    private List<String> ignoreFileNameThatIs = new ArrayList<>();

    /*
     * Ignore folder settings
     ********************************************************/
    private List<String> ignoreFolderNameThatStartWith = new ArrayList<>();
    private List<String> ignoreFolderNameThatEndWith = new ArrayList<>();
    private List<String> ignoreFolderNameThatContains = new ArrayList<>();
    private List<String> ignoreFolderNameThatIs = new ArrayList<>();


    public FileFinder isRecursive(boolean isRecursive){
        this.isSearchRecursive = isRecursive;

        return this;
    }


    public List<File> find(String path){
        this.selectFileCondition = buildSelectFileCondition();
        this.ignoreFileCondition = buildIgnoreFileCondition();
        this.ignoreFoldenCondition = buildIgnoreFolderCondition();

        List<File> all = findAllAndFilter(path);

        return all;
    }

    private List<File> findAllAndFilter(String path){
        List<File> list = new ArrayList<>();

        File file = new File(path);
        if (file.exists()){
            File[] files = file.listFiles();
            for (File f : Objects.requireNonNull(files)) {
                if (f.isDirectory()){
                    if (isSearchRecursive){
                        if (ignoreFoldenCondition.checkCondition(f.getName())){
                            list.addAll(findAllAndFilter(f.getAbsolutePath()));
                        }
                    }
                }else {
                    if (selectFileCondition.checkCondition(f.getName()) && ignoreFileCondition.checkCondition(f.getName())){
                        list.add(f);
                    }
                }
            }

        }
        return list;
    }

    private List<File> findAll(String path){
        List<File> list = new ArrayList<>();

        File file = new File(path);
        if (file.exists()){
            File[] files = file.listFiles();
            for (File f : Objects.requireNonNull(files)) {
                if (f.isDirectory()){
                    if (isSearchRecursive){
                        list.addAll(findAll(f.getAbsolutePath()));
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

    // SELECT FILE CONDITIONS

    public FileFinder withFileNameStartsWith(String... fileNames){
        fileNameStartsWith.addAll(Arrays.asList(fileNames));
        return this;
    }
    public FileFinder withFileNameStartsWith(Collection<String> fileNames){
        fileNameStartsWith.addAll(fileNames);
        return this;
    }

    public FileFinder withFileNameEndsWith(String... fileNames){
        fileNameEndsWith.addAll(Arrays.asList(fileNames));
        return this;
    }
    public FileFinder withFileNameEndsWith(Collection<String> fileNames){
        fileNameEndsWith.addAll(fileNames);
        return this;
    }

    public FileFinder withFileNameContains(String... fileNames){
        fileNameContains.addAll(Arrays.asList(fileNames));
        return this;
    }
    public FileFinder withFileNameContains(Collection<String> fileNames){
        fileNameContains.addAll(fileNames);
        return this;
    }

    public FileFinder withFileNameIs(String... fileNames){
        fileNameIs.addAll(Arrays.asList(fileNames));
        return this;
    }
    public FileFinder withFileNameIs(Collection<String> fileNames){
        fileNameIs.addAll(fileNames);
        return this;
    }

    private CheckCondition buildSelectFileCondition() {
        final CheckCondition checkFileNameStartWith = name -> {
            if (fileNameStartsWith.size() == 0){
                return true;
            }
            for (String start : fileNameStartsWith) {
                if (name.startsWith(start)){
                    return true;
                }
            }
            return false;
        };

        final CheckCondition checkFileNameEndWith = name -> {
            if (fileNameEndsWith.size() == 0){
                return true;
            }
            for (String end : fileNameEndsWith) {
                if (name.endsWith(end)){
                    return true;
                }
            }
            return false;
        };

        final CheckCondition checkFileContains = name -> {
            if (fileNameContains.size() == 0){
                return true;
            }
            for (String fileName : fileNameContains) {
                if (name.equals(fileName)){
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

        return name -> {
            boolean nameEnd = checkFileNameEndWith.checkCondition(name);
            boolean nameIs =checkFileNameIs.checkCondition(name);
            boolean nameStart =checkFileNameStartWith.checkCondition(name);
            boolean nameContains =checkFileContains.checkCondition(name);
            return nameEnd && nameIs && nameStart && nameContains;
        };
    }


    // IGNORE FILE CONDITIONS

    public FileFinder withIgnoreFileNameStartsWith(String... fileNames){
        ignoreFileNameThatStartWith.addAll(Arrays.asList(fileNames));
        return this;
    }
    public FileFinder withIgnoreFileNameStartsWith(Collection<String> fileNames){
        ignoreFileNameThatStartWith.addAll(fileNames);
        return this;
    }

    public FileFinder withIgnoreFileNameEndsWith(String... fileNames){
        ignoreFileNameThatEndWith.addAll(Arrays.asList(fileNames));
        return this;
    }
    public FileFinder withIgnoreFileNameEndsWith(Collection<String> fileNames){
        ignoreFileNameThatEndWith.addAll(fileNames);
        return this;
    }

    public FileFinder withIgnoreFileNameContains(String... fileNames){
        ignoreFileNameThatContains.addAll(Arrays.asList(fileNames));
        return this;
    }
    public FileFinder withIgnoreFileNameContains(Collection<String> fileNames){
        ignoreFileNameThatContains.addAll(fileNames);
        return this;
    }

    public FileFinder withIgnoreFileNameIs(String... fileNames){
        ignoreFileNameThatIs.addAll(Arrays.asList(fileNames));
        return this;
    }
    public FileFinder withIgnoreFileNameIs(Collection<String> fileNames){
        ignoreFileNameThatIs.addAll(fileNames);
        return this;
    }

    private CheckCondition buildIgnoreFileCondition() {
        final CheckCondition ignoreCheckFileNameStartWith = name -> {
            if (ignoreFileNameThatStartWith.size() == 0){
                return true;
            }
            for (String start : ignoreFileNameThatStartWith) {
                if (name.startsWith(start)){
                    return false;
                }
            }
            return true;
        };

        final CheckCondition ignoreCheckFileNameEndWith = name -> {
            if (ignoreFileNameThatEndWith.size() == 0){
                return true;
            }
            for (String end : ignoreFileNameThatEndWith) {
                if (name.endsWith(end)){
                    return false;
                }
            }
            return true;
        };

        final CheckCondition ignoreCheckFileContains = name -> {
            if (ignoreFileNameThatContains.size() == 0){
                return true;
            }
            for (String fileName : ignoreFileNameThatContains) {
                if (name.equals(fileName)){
                    return false;
                }
            }
            return true;
        };

        final CheckCondition ignoreCheckFileNameIs = name -> {
            if (ignoreFileNameThatIs.size() == 0){
                return true;
            }
            for (String fileName : ignoreFileNameThatIs) {
                if (name.equals(fileName)){
                    return false;
                }
            }
            return true;
        };

        return name -> {
            boolean nameEnd = ignoreCheckFileNameEndWith.checkCondition(name);
            boolean nameIs = ignoreCheckFileNameIs.checkCondition(name);
            boolean nameStart = ignoreCheckFileNameStartWith.checkCondition(name);
            boolean nameContains = ignoreCheckFileContains.checkCondition(name);
            return nameEnd && nameIs && nameStart && nameContains;
        };
    }


    // IGNORE Folder CONDITIONS

    public FileFinder withIgnoreFolderNameStartsWith(String... fileNames){
        ignoreFolderNameThatStartWith.addAll(Arrays.asList(fileNames));
        return this;
    }
    public FileFinder withIgnoreFolderNameStartsWith(Collection<String> fileNames){
        ignoreFolderNameThatStartWith.addAll(fileNames);
        return this;
    }

    public FileFinder withIgnoreFolderNameEndsWith(String... fileNames){
        ignoreFolderNameThatEndWith.addAll(Arrays.asList(fileNames));
        return this;
    }
    public FileFinder withIgnoreFolderNameEndsWith(Collection<String> fileNames){
        ignoreFolderNameThatEndWith.addAll(fileNames);
        return this;
    }

    public FileFinder withIgnoreFolderNameContains(String... fileNames){
        ignoreFolderNameThatContains.addAll(Arrays.asList(fileNames));
        return this;
    }
    public FileFinder withIgnoreFolderNameContains(Collection<String> fileNames){
        ignoreFolderNameThatContains.addAll(fileNames);
        return this;
    }

    public FileFinder withIgnoreFolderNameIs(String... fileNames){
        ignoreFolderNameThatIs.addAll(Arrays.asList(fileNames));
        return this;
    }
    public FileFinder withIgnoreFolderNameIs(Collection<String> fileNames){
        ignoreFolderNameThatIs.addAll(fileNames);
        return this;
    }

    private CheckCondition buildIgnoreFolderCondition() {
        final CheckCondition ignoreCheckFolderNameStartWith = name -> {
            if (ignoreFolderNameThatStartWith.size() == 0){
                return true;
            }
            for (String start : ignoreFolderNameThatStartWith) {
                if (name.startsWith(start)){
                    return false;
                }
            }
            return true;
        };

        final CheckCondition ignoreCheckFolderNameEndWith = name -> {
            if (ignoreFolderNameThatEndWith.size() == 0){
                return true;
            }
            for (String end : ignoreFolderNameThatEndWith) {
                if (name.endsWith(end)){
                    return false;
                }
            }
            return true;
        };

        final CheckCondition ignoreCheckFolderContains = name -> {
            if (ignoreFolderNameThatContains.size() == 0){
                return true;
            }
            for (String fileName : ignoreFolderNameThatContains) {
                if (name.equals(fileName)){
                    return false;
                }
            }
            return true;
        };

        final CheckCondition ignoreCheckFolderNameIs = name -> {
            if (ignoreFolderNameThatIs.size() == 0){
                return true;
            }
            for (String fileName : ignoreFolderNameThatIs) {
                if (name.equals(fileName)){
                    return false;
                }
            }
            return true;
        };

        return name -> {
            boolean nameEnd = ignoreCheckFolderNameEndWith.checkCondition(name);
            boolean nameIs = ignoreCheckFolderNameIs.checkCondition(name);
            boolean nameStart = ignoreCheckFolderNameStartWith.checkCondition(name);
            boolean nameContains = ignoreCheckFolderContains.checkCondition(name);
            return nameEnd && nameIs && nameStart && nameContains;
        };
    }


}