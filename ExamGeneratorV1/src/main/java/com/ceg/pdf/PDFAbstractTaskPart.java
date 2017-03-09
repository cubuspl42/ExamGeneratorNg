package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import com.ceg.utils.FontType;
import java.io.File;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

public abstract class PDFAbstractTaskPart {
    protected int lineHeight;
    protected static PDPageContentStream cs;
    protected int maxTextWidth;
    protected float actualWidth = 0;  
    protected int leftMargin = 0;
    protected List<PDFLine> pdfLines;
    protected int fontSize;
    protected FontType defaultFontType;
    
    PDFAbstractTaskPart() throws IOException{
        PDFSettings pdfSettings = PDFSettings.getInstance();
        cs = PDFGenerator.cs;
        pdfLines = new ArrayList<>();
        this.maxTextWidth = 0;
        lineHeight = pdfSettings.getCodeFontSize() + 1;
    }
    public int getNumberOfLines() {
        return pdfLines.size();
    }
    public int getLineHeight() {
       return lineHeight; 
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
    
    public void textSplitting (List<String> command) throws IOException, EmptyPartOfTaskException {
        if (command.isEmpty())
            throw new EmptyPartOfTaskException();
    }
    
    public int writeToPDF(int y, boolean lineNumbersVisibility) throws IOException, EmptyPartOfTaskException {
        if (pdfLines.isEmpty())
            throw new EmptyPartOfTaskException();
        
        for (PDFLine i : pdfLines) {
            i.writeLine(y);
            y -= lineHeight;
        }
        pdfLines.clear();
        return y;
    }
}
