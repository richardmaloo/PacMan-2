package src;
import java.io.File;
import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        // Specify the folder path
        String folderPath = "pacman/src/mapeditor/game";

        // Create a File object for the folder
        File folder = new File(folderPath);
        System.out.println(folder.getPath());

        // Get all the files in the folder
        File[] files = folder.listFiles();
        ArrayList<String> fileNames = new ArrayList<String>();

        // Iterate over the files and print their names
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    boolean startsWithNumber = fileName.matches("^\\d.*");
                    if(startsWithNumber)
                        fileNames.add(file.getName());
                }
            }
        }
        // Convert ArrayList to array
        String[] array = fileNames.toArray(new String[0]);

        // Print the elements of the array
        for (String element : array) {
            System.out.println(element);
        }
    }
}
