package com.ceg.utils;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * Umożliwia szybsze tworzenie okien wyboru plików do zapisu/odczytu
 */
public class FileChooserCreator {

    public enum FileType {
        XML, PDF, CODE
    }
    private final String XML_DESCRIPTION = "XML file (*.xml)";
    private final String XML_TYPE = ("*.xml");
    private final String PDF_DESCRIPTION = "PDF file (*pdf)";
    private final String PDF_TYPE = ("*.pdf");
    private final String CODE_DESCRIPTION = "Code files (*.txt, *.c, *.cpp)";
    private final String C_TYPE = ("*.c");
    private final String CPP_TYPE = ("*.cpp");
    private final String TXT_TYPE = ("*.txt");
    private static FileChooserCreator fileChooserCreator = new FileChooserCreator();
    private String initialDirectory;

    public String getInitialDirectory() {
        return initialDirectory;
    }
    public void setInitialDirectory(String initialDirectory) {
        this.initialDirectory = initialDirectory;
    }

    private FileChooserCreator() {}

    public static FileChooserCreator getInstance() { return fileChooserCreator; }

    /**
     * Wyświetla okno zapisu pliku.
     * @param stage Stage nad którym okno ma zostać wyświetlone.
     * @param type Typ pliku.
     * @return Zapisany plik (lub null, jeśli sie nie powiedzie).
     */
    public File createSaveDialog(Stage stage, FileType type, String defaultName) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(initialDirectory == null ? new File(System.getProperty
                ("user.home")) : new File(initialDirectory));
        fileChooser.getExtensionFilters().add(createExtensionFilter(type));
        fileChooser.setInitialFileName(defaultName);
        File file = fileChooser.showSaveDialog(stage);
        if(file != null)
            initialDirectory = file.getParent();
        return file;
    }

    /**
     * Wyświetla okno odczytu pliku.
     * @param stage Stage nad którym okno ma zostać wyświetlone.
     * @param type Typ pliku.
     * @return Odczytany plik (lub null, jesli się nie powiedzie).
     */
    public File createLoadDialog(Stage stage, FileType type) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(initialDirectory == null ? new File(System.getProperty
                ("user.home")) : new File(initialDirectory));
        fileChooser.getExtensionFilters().add(createExtensionFilter(type));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            initialDirectory = file.getParent();
        }
        return file;
    }

    private FileChooser.ExtensionFilter createExtensionFilter(FileType type) {
        switch(type) {
            case XML:
                return new FileChooser.ExtensionFilter(XML_DESCRIPTION, XML_TYPE);
            case PDF:
                return new FileChooser.ExtensionFilter(PDF_DESCRIPTION, PDF_TYPE);
            case CODE:
                return new FileChooser.ExtensionFilter(CODE_DESCRIPTION, C_TYPE, CPP_TYPE,
                        TXT_TYPE);
            default:
                return null;
        }
    }
}
