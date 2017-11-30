package pl.edu.pg.examgeneratorng.util;

import pl.edu.pg.examgeneratorng.Main;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.apache.commons.lang3.SystemUtils.IS_OS_LINUX;
import static org.apache.commons.lang3.SystemUtils.IS_OS_WINDOWS;

public class SystemUtils {
    public static Path windowsGccCompilerDirectory() {

        if (isNotWindows())
            throw new RuntimeException("This operating system is not windows.");

        try{
            return Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI())
                    .toAbsolutePath()
                    .getParent()
                    .resolve("MinGW")
                    .resolve("bin")
                    .toAbsolutePath();
        }
        catch(URISyntaxException uriSyntaxException) {

            throw new RuntimeException(uriSyntaxException);
        }
    }

    private static boolean isNotWindows() {

        return !IS_OS_WINDOWS;
    }

    public static Path rootDirectory() {

        try{
            Path currentDirectory = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());

            return currentDirectory.toAbsolutePath().getRoot();
        }
        catch(URISyntaxException uriSyntaxException) {

            throw new RuntimeException(uriSyntaxException);
        }
    }


    public static boolean isLinux() {

        return IS_OS_LINUX;
    }
}
