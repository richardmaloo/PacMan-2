package src;

import src.mapeditor.editor.Controller;
import src.utility.GameCallback;
import src.utility.PropertiesLoader;

import java.io.File;
import java.util.Properties;

public class Driver {
    public static final String DEFAULT_PROPERTIES_PATH = "pacman/properties/test.properties";
    private static boolean testMode = false;

    /**
     * Starting point
     * @param args the command line arguments
     */

    public static void main(String args[]) {
        System.out.println("Starting Pacman");
        String propertiesPath = DEFAULT_PROPERTIES_PATH;
        if (args.length == 0) {
            testMode = true;
            new Controller(null);
        } else {
            File path = new File(args[0]);

            if(!testMode && !path.isDirectory()){
                testMode = true;
                new Controller(path);
            } else {
                testMode = true;
                final Properties properties = PropertiesLoader.loadPropertiesFile(propertiesPath);
                GameCallback gameCallback = new GameCallback();
                System.out.println("Ready");

                new Game(gameCallback, properties, path);
                System.out.println("Finish");
            }
        }
    }
}

