package com.ceg.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;

public final class Alerts {
    
    /**
     * Wyświetla alert o niemieszczeniu się kodu w części kodu
     */
    public static void codeLineIsTooLong() {
        showAlert(AlertType.WARNING, "Ostrzeżenie", "Kod nie mieści się w wyznaczonym polu." , 
                "Zmień kod, zmniejsz czcionkę lub zmień szerokość pola kodu.");
    }
    /**
     * Alert z informacją, że pole z miejscem na odpowiedź w pdf jest zbyt wąskie
     */
    public static void tooNarrowPlaceForAnswer() {
        showAlert(AlertType.WARNING, "Uwaga", "Szerokość pola odpowiedzi.", "Pole z miejscem na odpowiedź w dokumencie \n"
                +  "jest zbyt wąskie.");
    }

    /**
     * Wyświetla okno z informacją o braku części zadania.
     */
    public static void emptyPartOfTaskAlert() {
        showAlert(AlertType.INFORMATION, "Uwaga!", "Brak części zadania.",
                "Sprawdź czy pola z poleceniem i kodem nie są puste \n"
                        + "oraz czy podana liczba odpowiedzi zgadza się ze stanem faktycznym.");
    }

    /**
     * Wyświetla okno z błędem dot. nieprawidłowego parsowania kodu.
     */
    public static void parsingCodeErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd podczas parsowania kodu.", "Sprawdź poprawność kodu.");
    }

    /**
     * Wyświetla okno z błędem dot. nieprawidłowego wygenerowania odpowiedzi.
     */
    public static void generatingAnswersErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd podczas generowania odpowiedzi.", "Sprawdź poprawność kodu.");
    }

    /**
     * Wyświetla okno z błędem dot. błedu wykonania programu.
     */
    public static void executionErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd wykonania programu.", "Sprawdź poprawność kodu.");
    }
    
    /**
     * Wyświetla okno z błędem dot. przekroczenia limitu czasu wykonania programu.
     */
    public static void executionTimetoutErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Przekroczenie limitu czasu.", "Nastąpiło przekroczenie limitu czasu wykonania programu.");
    }
    
    /**
     * Wyświetla okno z błędem dot. generowania pdf'a dla pustego arkusza.
     */
    public static void emptyExamAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Egzamin jest pusty.", "Nie można wygenerować pustego arkusza, dodaj zadania.");
    }

    /**
     * Wyświetla okno z błędem dot. niepoprawnych danych w pliku.
     */
    public static void wrongFileContentAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nieprawidłowa zawartość pliku.", "Plik zawiera nieodpowiednie znaczniki.");
    }

    /**
     * Wyświetla okno z informacją o błędzie kompilacji podczas automatyczbej kompilacji egzaminu
     */
    public static void compileErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd kompilacji", "Popraw błędy w kodzie dolączonym do zadania.");
    }

    /**
     * Wyświetla okno z informacją o błędzie przy zapisie egzaminu do pliku
     */
    public static void examSavingErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd zapisu", "Nie udało się zapisać egzaminu do pliku.");
    }

    /**
     * Wyświetla okno z informacją o błędzie przy zapisie zadania do pliku
     */
    public static void taskSavingErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd zapisu", "Nie udało się zapisać zadania do pliku.");
    }

    /**
     * Wyświetla okno z informacją o błędzie przy odczycie egzaminu z pliku
     */
    public static void examLoadingErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd odczytu", "Nie udało się odczytać egzaminu z pliku.");
    }

    /**
     * Wyświetla okno z informacją o błędzie przy odczycie zadania z pliku
     */
    public static void taskLoadingErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd odczytu", "Nie udało się odczytać zadania z pliku.");
    }

    /**
     * Wyświetla okno z informacją o błędzie przy dodawaniu CSS Styles
     */
    public static void stylesLoadingErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd przy dodawaniu reguł CSS",  "Nie udało się dodać stylów CSS do okna głównego programu.");
    }

    /**
     * Wyświetla okno z informacją o błędzie przy aktualizowaniu tekstu polecenia
     */
    public static void updateTextErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd przy aktualizowaniu tekstu polecenia",  "Nie udało się zaktualizować polecenia.");
    }

    /**
     * Wyświetla okno z informacją o błędzie przy wczytywaniu tekstu do pola polecenia
     */
    public static void addTypeErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd przy wczytywaniu polecenia",  "Nie udało się wczytać tekstu do pola z poleceniem.");
    }

    /**
     * Wyświetla okno z informacją o błędzie po wybraniu pliku, przy ładowaniu kodu
     */
    public static void codeLoadingErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd przy ładowaniu pliku z kodem",  "Nie udało się wczytać kodu z pliku.");
    }

    /**
     * Wyświetla okno z informacją o błędzie przy kompilacji
     */
    public static void fileCompileErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd przy kompilacji",  "Nie udało się skompilować pliku.");
    }

    /**
     * Wyświetla okno z informacją o pustym pliku
     */
    public static void emptyFileAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd przy kompilacji",  "Plik, który miał zostać skompilowany, jest pusty.");
    }

    /**
     * Wyświetla okno z informacją o błędzie przy tworzeniu obiektu kompilatora
     */
    public static void createCompilerErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd przy kompilacji",  "Nie udało się stworzyć obiektu kompilatora.");
    }

    /**
     * Wyświetla okno z informacją o błędzie przy wykonaniu
     */
    public static void executeErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd przy wykonaniu",  "Nie udało się skompilować i wykonać pliku.");
    }

    /**
     * Wyświetla okno z informacją o błędzie przy wydobywaniu kodu do kompilacji z obiektu Text
     */
    public static void getCodeToCompileErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd przy pobieraniu kodu",  "Nie udało się pobrać kodu do kompilacji.");
    }

    /**
     * Wyświetla okno z informacją o błędzie przy wydobywaniu kodu do tworzenia pliku .pdf
     */
    public static void getCodeToPdfErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd przy pobieraniu kodu",  "Nie udało się pobrać kodu do tworzenia pliku .pdf.");
    }

    /**
     * Wyświetla okno z informacją o błędzie przy otwieraniu ustawień zaawansowanych
     */
    public static void advancedOptionsErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd przy otwieraniu okna",  "Nie udało się otworzyć okna ustawień zaawansowanych.");
    }

    /**
     * Wyświetla okno z błędem dot. próby zapisu do otwartego pliku.
     */
    public static void fileAlreadyOpened() {
        showAlert(AlertType.ERROR, "Błąd", "Nie można zapisać pliku", "Plik jest już otwarty, zamknij go i spróbuj ponownie.");
    }

     /**
     * Wyświetla okno z informacją o poprawnej generacji pliku PDF.
    */
    public static void fileGenerated() {
        showAlert(AlertType.INFORMATION, "Zapisano", "Operacja zakończona pomyślnie", "Wygenerowano plik PDF.");
    }
     /**
     * Wyświetla okno z informacją o błędzie przy zapisie poleceń do pliku
     */
    public static void taskDataSavingErrorAlert() {
        showAlert(AlertType.ERROR, "Błąd", "Nastąpił błąd zapisu", "Nie udało się zapisać poleceń do pliku.");
    }


    /**
     * Wyświetla okno z alertem.
     *
     * @param alertType   Typ alertu.
     * @param title       Tytuł okna.
     * @param headerText  Tekst nagłówka okna.
     * @param contextText Wiadomość zawarta w oknie.
     */
    private static void showAlert(AlertType alertType, String title, String headerText, String contextText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contextText);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);

        alert.showAndWait();
    }
}
