package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;

import java.io.IOException;
import java.util.List;


public class PDFTeachersGapsCode extends PDFGapsCode {
    PDFTeachersGapsCode(List<String> lines, float pdfContentWidthPercentage) throws IOException, EmptyPartOfTaskException {
        super(lines, pdfContentWidthPercentage);
        answer = new PDFGapsTeachersAnswer(lines, maxTextWidth, defaultFontType, fontSize, leftMargin);
    }
}
