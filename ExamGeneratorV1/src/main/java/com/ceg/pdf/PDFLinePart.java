package com.ceg.pdf;

import static com.ceg.pdf.PDFGenerator.cs;
import com.ceg.utils.FontType;
import java.io.File;
import java.io.IOException;
import java.awt.Color;
import org.apache.pdfbox.pdmodel.font.PDType0Font;


public class PDFLinePart {
    private final FontType font;
    private static final float underlineWidth = 0.1f;
    private String text;
    private boolean underlined;
    
    public PDFLinePart(FontType font) {
        this.font = font;
        text = "";
        underlined = false;
    }
    
    public PDFLinePart(FontType font, boolean underlined) {
        this.font = font;
        text="";
        this.underlined = underlined;
    }
    
    public float writeLinePart(float x, int y, int fontSize, Color color) throws IOException {
        float result = x;
        if (!text.equals("")) {
            result = x + getWidth(fontSize);
            cs.beginText();
            cs.newLineAtOffset(x, y);
            cs.setNonStrokingColor(color);
            cs.setFont(PDType0Font.load(PDFGenerator.document, new File(font.getFileName())), fontSize);
            cs.showText(text);
            cs.endText();
            if (underlined) {
                cs.moveTo(x, y - 1);
                cs.lineTo(result, y - 1);
                cs.setLineWidth(underlineWidth);
                cs.stroke();
            }
        }
        
        return result;
    }
    
    /**
     * Oblicza szerokość tekstu napisanego czcionką o konkretnym rozmiarze.
     * @param fontSize Rozmiar czcionki, dla której obliczana jest szerokość tekstu.
     * @return
     * @throws IOException
     */
    protected float getWidth(int fontSize) throws IOException {
        return PDType0Font.load(PDFGenerator.document, new File(font.getFileName())).getStringWidth(text) / 1000 * fontSize;
    }
    
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }
    
    public void setUnderline(boolean underlined) {
        this.underlined = underlined;
    }
}
