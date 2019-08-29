package resources.model;


import java.util.ArrayList;
import java.util.List;

public class Folder {

    private String name;
    private List<Folder> folders;
    private String absolutePath;
    private String relativePath;
    private List<String> fileNames;

    public String getRelativePath() {
        return relativePath;
    }

    public void setRelativePath(String relativePath) {
        this.relativePath = relativePath;
    }

    public String getName() {
        return name;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public List<String> getFiles() {
        return fileNames;
    }

    public Folder() {
        folders = new ArrayList<>();
        fileNames = new ArrayList<>();
    }

    public Folder(String newFolderName) {
        this.name = newFolderName;
        folders = new ArrayList<>();
        fileNames = new ArrayList<>();
    }


    public boolean hasLocalFolder(String newFolderName) {
        for (Folder folder : folders) {
            if (folder.name.equals(newFolderName)) {
                return true;
            }
        }
        return false;
    }

    public void addFile(String file) {
        System.out.println("Added file " + file);
        fileNames.add(file);
    }

    public Folder getLocalFolder(String newFolderName) {
        for (Folder folder : folders) {
            if (folder.name.equals(newFolderName)) {
                return folder;
            }
        }
        throw new RuntimeException("No folder");
    }

    public void print(String padding) {
        System.out.println(padding + name);
        System.out.println(padding + fileNames);
        System.out.println(padding + absolutePath);
        for (Folder folder : folders) {
            folder.print(padding + "\t");
        }
    }

    public void addFolder(Folder newFolder) {
        this.getFolders().add(newFolder);
    }

    public void setAbsolutePath(String parent) {
        this.absolutePath = parent;
    }

    public String getAbsolutePath() {
        return this.absolutePath;
    }
}