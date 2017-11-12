package pl.edu.pg.examgeneratorng;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingConfig {
    public static void configureLogging() {
        Logger.getLogger("org.odftoolkit.odfdom.pkg.OdfXMLFactory").setLevel(Level.WARNING);
    }
}
