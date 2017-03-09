package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import com.ceg.utils.FontType;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Martyna
 */
public class PDFGapsAnswer extends PDFAnswer { 
    
    public PDFGapsAnswer(List<String> lines, int textWidth, FontType font, int fontSize, int leftMargin) throws IOException {
        super(lines, textWidth, font, fontSize, leftMargin);
    }
    
    @Override
    public void setAnswers(List<String> answers) {         
        answers.stream().map((s) -> s.length() + 1).map((placeForAnswerLength) -> {
            String placeForAnswer = "";
            for (int i = 0; i < placeForAnswerLength; i++) {
                placeForAnswer += '_';
            }
            return placeForAnswer;
        }).forEach((placeForAnswer) -> {
            placesForAnswers.add(placeForAnswer);
        });
    }   
    
    @Override
    public int writeToPDF(int y, boolean lineNumbersVisibility) throws IOException, EmptyPartOfTaskException {
        if (lineNumbersVisibility) {
            for (int i = 0; i < pdfLines.size(); i++) {
                pdfLines.get(i).getLineParts().get(0).setText(i+1 + ":  " + pdfLines.get(i).getLineParts().get(0).getText());
            }
        }
        
        int tempY = super.writeToPDF(y, lineNumbersVisibility);
        
        if (lineNumbersVisibility) {
            float width = leftMargin + getWidth("   ", defaultFontType, fontSize);
            
            PDFGenerator.cs.moveTo(width, tempY);
            PDFGenerator.cs.lineTo(width, y + lineHeight);
            PDFGenerator.cs.setLineWidth(PDFSettings.getInstance().getSeparatorWidth());
            PDFGenerator.cs.stroke();
        }
        
        return tempY;
    }
}
