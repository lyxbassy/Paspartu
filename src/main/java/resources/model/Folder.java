package resources.model;


import java.io.File;
import java.util.*;

public class Folder {
    File folder;
    Folder parentFolder;
    File headPath;

    Map<String, Folder> folders = new HashMap<>();
    List<File> files = new ArrayList<>();

    public Folder (String headPath){
        this.headPath = new File(headPath);
    }

    public Folder (File headPath){
        this.headPath = headPath;
    }

    public Folder (){
    }

    public File getHeadPath() {
        return headPath;
    }

    public void setHeadPath(File headPath) {
        this.headPath = headPath;
    }

    public void add(File f){
        String filepath = f.getAbsolutePath().substring(headPath.getAbsolutePath().length());

        String[] split = filepath.split(File.separator);

        Folder folderToAdd = this;
        for (int i = 0; i < split.length - 1; i++) {
            String folderName = split[i];
            if (!folderToAdd.getFolders().containsKey(folderName)){
                folderToAdd.getFolders().put(folderName, new Folder(headPath));
            }

            Folder parentFolder = folderToAdd;
            folderToAdd = folderToAdd.getFolders().get(folderName);
            folderToAdd.setParentFolder(parentFolder);
        }

        folderToAdd.getFiles().add(f);
        System.out.println(Arrays.toString(split));
    }

    public Map<String, Folder> getFolders() {
        return folders;
    }

    public void setFolders(Map<String, Folder> folders) {
        this.folders = folders;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public File getFolder() {
        return folder;
    }

    public void setFolder(File folder) {
        this.folder = folder;
    }

    public Folder getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(Folder parentFolder) {
        this.parentFolder = parentFolder;
    }

    @Override
    public String toString() {
        return "Folder{" +
                "folder=" + folder +
                ", folders=" + folders +
                ", files=" + files +
                '}';
    }
}