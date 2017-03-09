package com.ceg.pdf;

import com.ceg.examContent.Content;
import com.ceg.examContent.ContentPart;
import com.ceg.exceptions.EmptyPartOfTaskException;
import com.ceg.utils.ContentCssClass;
import com.ceg.utils.FontType;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PDFCommand extends PDFAbstractTaskPart {
    protected String[] words;
    protected float spaceWidth;
    private final Integer taskNumber;
        
    PDFCommand(Content content, int taskNumber) throws IOException, EmptyPartOfTaskException {
        super();
        this.taskNumber = taskNumber;
        PDFSettings pdfSettings = PDFSettings.getInstance();
        maxTextWidth = (int)Math.floor(content.getPdfWidthPercentage() * PDFSettings.pdfContentWidth);
        fontSize = pdfSettings.getCommandFontSize();
        defaultFontType = pdfSettings.getCommandFont();
        leftMargin = pdfSettings.leftMargin;
        contentSplitting(content.getContentParts());
    }
    
    /*  Funkcja odpowiedzialna za formatowanie tekstu polecenia. Argumentem jest tekst polecenia.
        Dzieli wyrazy po spacji i układa jak najwięcej w jednej linii. Wrażliwa na znaki entera:
        każdy enter w poleceniu będzie widoczny w dokumencie pdf. Brak możliwości używania innych
        białych znaków poza spacją i enterem. */    
    public void contentSplitting (List<ContentPart> contentParts) throws IOException, EmptyPartOfTaskException {
        List<ContentPart> command = new ArrayList<>(contentParts);
        actualWidth = 0;       
        command.add(0, new ContentPart(ContentCssClass.EMPTY, taskNumber.toString() + ". "));
        float actualWordWidth;
        
        PDFLine pdfLine = new PDFLine(fontSize, leftMargin);
        
        for (ContentPart cp : command) {
            FontType ft = defaultFontType.contentCssClassToFontType(cp.getCssClassName());
            PDFLinePart lp = new PDFLinePart(ft, cp.getCssClassName().isUnderlined());
            spaceWidth = getWidth(" ", ft, fontSize);
            String withSpaces = " " + cp.getText()+ " ";
            words = withSpaces.split("\n");
            
            if (cp.getText().charAt(0) == ' ') {
                lp.setText(" ");
            }    
            for (int i = 0; i < words.length; i++) {
                String[] spaceSplit = words[i].split(" ");
                for (String word : spaceSplit) {
                    actualWordWidth = getWidth(word, ft, fontSize);
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
                        pdfLine = new PDFLine(fontSize, leftMargin);
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
}
