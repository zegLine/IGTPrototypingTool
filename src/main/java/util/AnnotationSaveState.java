package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Class for the Save State.
 * This class uses the Singleton pattern to ensure only one instance of the save state is created.
 */
public class AnnotationSaveState {

    private static AnnotationSaveState instance = new AnnotationSaveState();
    private Path path;

    private AnnotationSaveState(){
        this.path = Path.of(System.getProperty(" tmpdir"));
    }

    /**
     * Function to save the current state
     * @param files
     */
    public void save(String[] files){
        //Todo creat a file format to save the state
        try{
            Path pathToTemp = Files.createTempFile(path, "IGT_", null);
            File file = new File(pathToTemp.toString());
        }catch (IOException e){
            System.out.println("Error creating temp file");
            e.printStackTrace();
        }
    }

    /**
     * Todo create a return type to work with
     *
     * @throws IOException
     */
    public void load() throws IOException {
        if(!savedFileExist()){
            //return null;
        }
        //Todo return file content
    }

    private boolean savedFileExist(){
        try {
            File dir = new File(path.toString());
            if (dir.isDirectory()) {
                for (File file : dir.listFiles()) {
                    if (file.isFile() && file.getName().contains("IGT_")) {
                        return true;
                    }
                }
            }
        } catch (SecurityException e) {
            // Handle security exceptions if accessing the directory is restricted
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     *
     * @return the instance of the Service to work with
     */
    public static AnnotationSaveState getInstance(){
        return instance;
    }

}
