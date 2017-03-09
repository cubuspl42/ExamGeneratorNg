package com.ceg.pdf;

import com.ceg.examContent.ContentPart;
import com.ceg.examContent.Exam;
import com.ceg.exceptions.EmptyPartOfTaskException;
import com.ceg.utils.Alerts;
import com.ceg.utils.ColorPicker;
import com.ceg.utils.ContentCssClass;
import com.ceg.utils.FontType;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import static com.ceg.utils.FontType.SERIF_REGULAR;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class PDFHeader {
    private final FontType fontType = SERIF_REGULAR;
    private final int fontSize = 12;
    private final int placeForIndexNumberX = 400;
    private final int placeForDateX = 501;
    private static int topMargin = PDFSettings.getInstance().topMargin;
    private static final int leftMargin = PDFSettings.getInstance().leftMargin;
    private static final int rightMargin = PDFSettings.rightMargin;
    private static final String testDate = PDFSettings.getInstance().getDate();
    private List<PDFLine> pdfLines = new ArrayList<>();
    protected int titleFontSize;
    protected FontType titleFontType;
    protected ColorPicker titleColor;
    protected int commentFontSize;
    protected FontType commentFontType;
    protected ColorPicker commentColor;
    
        /* Funkcja dodająca nagłówek postaci miejsc na imię  i nazwisko oraz numer indeksu studenta */
    public int setHeader(int breakAfterHeader) throws IOException {
        int top = topMargin;
        PDFLine line;
        PDFLinePart linePart;
        titleFontSize = PDFSettings.getInstance().getTitleFontSize();
        titleFontType = PDFSettings.getInstance().getTitleFont();
        titleColor = PDFSettings.getInstance().getTitleColor();
        commentFontSize = PDFSettings.getInstance().getCommentFontSize();
        commentFontType = PDFSettings.getInstance().getCommentFont();
        commentColor = PDFSettings.getInstance().getCommentColor();
        setFontTypeBoldOrItalic();

        if (!Exam.getInstance().getTitleContent().getContentParts().isEmpty()) {
            try {
                contentSplitting(Exam.getInstance().getTitleContent().getContentParts(), titleFontType,titleFontSize);
                for (PDFLine p : pdfLines) {
                    p.writeLineInColor(top, titleColor.getColor());
                    top -= titleFontSize + 2;
                }
            } catch (EmptyPartOfTaskException e) {
                Alerts.emptyPartOfTaskAlert();
            }
        }

        line = new PDFLine(fontSize, leftMargin);
        linePart = new PDFLinePart(fontType);
        linePart.setText("________________________________________________________");
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(top);
       
        line = new PDFLine(fontSize, placeForIndexNumberX);
        linePart = new PDFLinePart(fontType);
        linePart.setText("___________");
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(top);

        line = new PDFLine(fontSize, placeForDateX);
        linePart = new PDFLinePart(fontType);
        linePart.setText(testDate);
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(top);

        //przejście do następnej linii
        top -= 15;
        
        line = new PDFLine(fontSize, leftMargin + 1);
        linePart = new PDFLinePart(fontType);
        linePart.setText("Imię i nazwisko");
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(top);

        line = new PDFLine(fontSize, placeForIndexNumberX + 1);
        linePart = new PDFLinePart(fontType);
        linePart.setText("Nr indeksu");
        line.setLineParts(Arrays.asList(linePart));
        line.writeLine(top);

        if (!Exam.getInstance().getCommentContent().getContentParts().isEmpty()) {
            top -= 30;
            try {
                contentSplitting(Exam.getInstance().getCommentContent().getContentParts(), commentFontType,commentFontSize);
                for (PDFLine p : pdfLines) {
                    p.writeLineInColor(top, commentColor.getColor());
                    top -= commentFontSize + 2;
                }
                top += commentFontSize + 2;
            } catch (EmptyPartOfTaskException e) {
                Alerts.emptyPartOfTaskAlert();
            }
        }

        return top-breakAfterHeader;
    }
    
    /*  Funkcja odpowiedzialna za formatowanie tekstu. Argumentem jest tekst polecenia.
        Dzieli wyrazy po spacji i układa jak najwięcej w jednej linii. Wrażliwa na znaki entera:
        każdy enter w poleceniu będzie widoczny w dokumencie pdf. Brak możliwości używania innych
        białych znaków poza spacją i enterem. */    
    public void contentSplitting (List<ContentPart> contentParts, FontType fType, int fSize) throws IOException, EmptyPartOfTaskException {
        pdfLines.clear();
        List<ContentPart> command = new ArrayList<>(contentParts);
        float actualWidth = 0;       
        float actualWordWidth;
        float spaceWidth;
        String[] words;
        float maxTextWidth = rightMargin - leftMargin;
        
        PDFLine pdfLine = new PDFLine(fSize, leftMargin);
        
        for (ContentPart cp : command) {
            FontType ft = fType.contentCssClassToFontType(cp.getCssClassName());
            PDFLinePart lp = new PDFLinePart(ft, cp.getCssClassName().isUnderlined());
            spaceWidth = getWidth(" ", ft, fSize);
            String withSpaces = " " + cp.getText()+ " ";
            words = withSpaces.split("\n");
            
            if (cp.getText().charAt(0) == ' ') {
                lp.setText(" ");
            }    
            for (int i = 0; i < words.length; i++) {
                String[] spaceSplit = words[i].split(" ");
                for (String word : spaceSplit) {
                    actualWordWidth = getWidth(word, ft, fSize);
                    word = word.replaceAll("\r", "");

                     //linia wystarczająco długa, żaden wyraz więcej się nie zmieści
                    if (actualWidth + actualWordWidth + spaceWidth  >= maxTextWidth) {
                        //jeśli ostatnim znakiem w linii jest spacja to usuwamy ją

                        //jeśli spacja występuje w zakończonym PDFLinePart, zaktualizujemy poprzedni
                        if (lp.getText().length() == 0) {
                            PDFLinePart lp2 = pdfLine.getLineParts().get(pdfLine.getLineParts().size() - 1);
                            if (lp2.getText().charAt(lp2.getText().length() - 1) == ' ') {
                                lp2.setText(lp2.getText().substring(0, lp2.getText().length() - 2));
                                pdfLine.getLineParts().set(pdfLine.getLineParts().size() - 1, lp2);
                            }
                        }
                        //jeśli spacja występuje w aktualnym PDFLinePart to usuwamy na bieżąco                
                        else if (lp.getText().charAt(lp.getText().length() - 1) == ' ') {
                            lp.setText(lp.getText().substring(0, lp.getText().length() - 1));
                        }
                        pdfLine.getLineParts().add(lp);
                        pdfLines.add(pdfLine);
                        lp = new PDFLinePart(ft, cp.getCssClassName().isUnderlined());
                        pdfLine = new PDFLine(fSize, leftMargin);
                        lp.setText(lp.getText() + word);

                        actualWidth = actualWordWidth;
                    }
                    //do linii dopisujemy wyraz
                    else {
                        //jeśli linia nie jest pusta to dodajemy spację po poprzednim wyrazie
                        if (!lp.getText().equals("") && !lp.getText().equals(" ")) {
                            lp.setText(lp.getText() + ' ');
                            actualWidth += spaceWidth;
                        }
                        lp.setText(lp.getText() + word);
                        actualWidth += actualWordWidth;
                    }
                }
                if (cp.getText().charAt(cp.getText().length() - 1) == ' ' && 
                        lp.getText().charAt(lp.getText().length() - 1) != ' ') {
                    lp.setText(lp.getText() + ' ');
                }
                //jeśli linia nie jest pusta, wypisujemy ją
                if (!lp.getText().equals("")) {
                    pdfLine.getLineParts().add(lp);
                }
                if (i < words.length - 1) {
                    pdfLines.add(pdfLine);
                    lp = new PDFLinePart(ft, cp.getCssClassName().isUnderlined());
                    pdfLine = new PDFLine(fontSize, leftMargin);
                    lp.setText("");

                    actualWidth = 0;
                }
            }
        }
        pdfLines.add(pdfLine);
        pdfLines.add(new PDFLine(12, leftMargin));
    }  
 
    /**
     * Oblicza szerokość tekstu napisanego czcionką o konkretnym rozmiarze.
     * @param text Tekst dla którego jest obliczana szerokość.
     * @param fontType Rodzaj czcionki, dla której obliczana jest szerokość tekstu.
     * @param fontSize Rozmiar czcionki, dla której obliczana jest szerokość tekstu.
     * @return
     * @throws IOException
     */
    protected float getWidth(String text, FontType fontType, int fontSize) throws IOException {
        return PDType0Font.load(PDFGenerator.document, new File(fontType.getFileName())).getStringWidth(text) / 1000 * fontSize;
    }

    private void setFontTypeBoldOrItalic() {
        if (PDFSettings.getInstance().getIsTitleBold() && PDFSettings.getInstance().getIsTitleItalic()) {
            titleFontType = titleFontType.contentCssClassToFontType(ContentCssClass.BOLD_ITALIC);
        }
        else if (PDFSettings.getInstance().getIsTitleBold()) {
            titleFontType = titleFontType.contentCssClassToFontType(ContentCssClass.BOLD);
        }
        else if (PDFSettings.getInstance().getIsTitleItalic()) {
            titleFontType = titleFontType.contentCssClassToFontType(ContentCssClass.ITALIC);
        }

        if (PDFSettings.getInstance().getIsCommentBold() && PDFSettings.getInstance().getIsCommentItalic()) {
            commentFontType = commentFontType.contentCssClassToFontType(ContentCssClass.BOLD_ITALIC);
        }
        else if (PDFSettings.getInstance().getIsCommentBold()) {
            commentFontType = commentFontType.contentCssClassToFontType(ContentCssClass.BOLD);
        }
        else if (PDFSettings.getInstance().getIsCommentItalic()) {
            commentFontType = commentFontType.contentCssClassToFontType(ContentCssClass.ITALIC);
        }
    }
}
