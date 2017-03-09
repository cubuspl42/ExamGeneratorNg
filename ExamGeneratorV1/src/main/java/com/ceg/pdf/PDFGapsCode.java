package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;

import java.io.IOException;
import java.util.List;


public class PDFGapsCode extends PDFCode {
    private final List<String> lines;

    PDFGapsCode(List<String> lines, float pdfContentWidthPercentage) throws IOException, EmptyPartOfTaskException {
        super(lines, pdfContentWidthPercentage, false);
        answer = new PDFGapsAnswer(lines, maxTextWidth, defaultFontType, fontSize, leftMargin);
        this.lines = lines;
    }

    @Override
    public int writeToPDF(int y, boolean lineNumbersVisibility) throws IOException, EmptyPartOfTaskException {
        this.showAlerts = true;
        this.textSplitting(lines);
        answer.pdfLines = this.pdfLines;
        return answer.writeToPDF(y, lineNumbersVisibility);
    }
}
