package pl.edu.pg.examgeneratorng.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public static String readWholeFile(Path filePath) {
        try {
            return new String(Files.readAllBytes(filePath));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }


    public static Path directoryWithTemporaryFiles() {

        String temporaryDirectory = System.getProperty("java.io.tmpdir");

        return Paths.get(temporaryDirectory);
    }
}
