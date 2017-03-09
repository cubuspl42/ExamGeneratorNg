package com.ceg.pdf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;


public class PDFLine {
    private List<PDFLinePart> lineParts;
    private final int fontSize;
    private final int leftMargin;
    
    public PDFLine(int fontSize, int leftMargin) throws IOException {
        this.fontSize = fontSize;
        this.lineParts = new ArrayList<>();
        this.leftMargin = leftMargin;
    }
    
    public void writeLine(int startY) throws IOException { 
        writeLineInColor(startY, Color.BLACK);
    }
    
    public void writeLineInColor(int startY, Color color) throws IOException {
        float x = leftMargin;
        for (PDFLinePart lp : lineParts) {
            x = lp.writeLinePart(x, startY, fontSize, color);
        }
    }
    
    public int getFontSize() {
        return fontSize;
    }
    
    public List<PDFLinePart> getLineParts() {
        return lineParts;
    }
    
    public void setLineParts(List<PDFLinePart> lineParts) {
        this.lineParts = lineParts;
    }
}
