package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import com.ceg.utils.Alerts;
import javafx.application.Platform;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import javafx.application.Platform;

public class PDFCode extends PDFAbstractTaskPart {
    private String line;
    public PDFAnswer answer = null;
    protected boolean showAlerts = true;
    
    private final List<String> operatorList = Arrays.asList("//", 
                                                            ";", 
                                                            ",", 
                                                            "{", 
                                                            "(", 
                                                            "}", 
                                                            ")", 
                                                            "<<",
                                                            ">>",
                                                            "==",
                                                            "=",
                                                            ">",
                                                            "<",
                                                            " " );
    
    PDFCode(List<String> lines, float codeWidthPercentage) throws IOException, EmptyPartOfTaskException {
        super();
        PDFSettings pdfSettings = PDFSettings.getInstance();

        maxTextWidth = (int)Math.ceil(pdfSettings.pdfContentWidth * codeWidthPercentage);
        leftMargin = pdfSettings.rightMargin - maxTextWidth;
        defaultFontType = pdfSettings.getCodeFont();
        fontSize = pdfSettings.getCodeFontSize();
        textSplitting(lines);
    }
    
    PDFCode(List<String> lines, float codeWidthPercentage, boolean showAlerts) throws IOException, EmptyPartOfTaskException {
        super();
        this.showAlerts = showAlerts;
        PDFSettings pdfSettings = PDFSettings.getInstance();

        maxTextWidth = (int)Math.ceil(pdfSettings.pdfContentWidth * codeWidthPercentage);
        leftMargin = pdfSettings.rightMargin - maxTextWidth;
        defaultFontType = pdfSettings.getCodeFont();
        fontSize = pdfSettings.getCodeFontSize();
        textSplitting(lines);
    }
    
    /*  Funkcja formatująca kod do pdfa. Jako parametr przyjmuje listę linii kodu.
        Reaguje na spacje i tabulatory (jeśli używa się albo jednego albo drugiego.
        Nie przesyłać z enterami.    */   
    @Override
    public void textSplitting (List<String> codeLines) throws IOException, EmptyPartOfTaskException {
        pdfLines.clear();
        super.textSplitting(codeLines);
        float actualLineWidth;
        boolean alert = false;
        int numberOfAnswer = 0;
        
        for (String codeLine : codeLines) { 
            if (answer != null && codeLine.contains("#placeForAnswer")) {
                line = codeLine.replace("#placeForAnswer", answer.placesForAnswers.get(numberOfAnswer++));
            }
            else {
                line = codeLine;
            }
            line = ifTabDoTab(line);
            codeLine = ifTabDoTab(codeLine);
            actualLineWidth = getWidth(line, defaultFontType, fontSize); 
            
            if (actualLineWidth <= maxTextWidth) {
                PDFLine pdfLine = new PDFLine(fontSize, leftMargin);
                PDFLinePart lp = new PDFLinePart(defaultFontType);
                lp.setText(codeLine);
                pdfLine.setLineParts(Arrays.asList(lp));
                pdfLines.add(pdfLine);
            }
            
            else {
                if (!alert && showAlerts) {
                     Platform.runLater(new Runnable(){ 
                            @Override
                            public void run() {
                              Alerts.codeLineIsTooLong();
                          }
                     });
                    alert = true;
                }
                for (String string : operatorList) {
                    if (splittingLine(string))
                        break;
                }
            }
        }
    }
    
    /*  Zamienia każdy tabulator na odpowiadającą mu ilość spacji   */
    private String ifTabDoTab(String line) {
        return line.replace("\t", "    ");
    }
    
    private boolean splittingLine(String operator) throws IOException {
        if (!line.contains(operator))
            return false;
        else {
            int lineLength = line.length();
            
            while (lineLength != 0) {
                int i = line.lastIndexOf(operator, lineLength);
                if (i < 0)
                    return false;
                String end;
                end = line.substring(0, i+1);
                if (getWidth(end, defaultFontType, fontSize) <= maxTextWidth) {
                    PDFLine pdfLine = new PDFLine(fontSize, leftMargin);
                    PDFLinePart lp = new PDFLinePart(defaultFontType);
                    lp.setText(end);
                    pdfLine.setLineParts(Arrays.asList(lp));
                    pdfLines.add(pdfLine);
                    line = line.substring(i+1);
                    i = line.length();
                    if (getWidth(line, defaultFontType, fontSize) <= maxTextWidth) {
                        pdfLine = new PDFLine(fontSize, leftMargin);
                        lp = new PDFLinePart(defaultFontType);
                        lp.setText(line);
                        pdfLine.setLineParts(Arrays.asList(lp));
                        pdfLines.add(pdfLine);
                        return true;
                    }
                }
                lineLength = i - 1;
            }
        }
        return true;
    }

    protected void setAnswer(PDFAnswer answer) {
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
