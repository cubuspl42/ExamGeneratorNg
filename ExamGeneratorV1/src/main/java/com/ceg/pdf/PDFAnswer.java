package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import com.ceg.utils.Alerts;
import com.ceg.utils.FontType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/* klasa odpowiedzialna za wyglad pola odpowiedzi w arkuszu pdf */
public class PDFAnswer extends PDFAbstractTaskPart {
    protected List<String> placesForAnswers = new ArrayList<>();
    private String placeForAnswer = "";

    public PDFAnswer(List<String> lines, float pdfContentWidthPercentage) throws IOException {
        super();
        PDFSettings pdfSettings = PDFSettings.getInstance();
        maxTextWidth = (int)Math.floor(pdfContentWidthPercentage * PDFSettings.pdfContentWidth);;
        leftMargin = pdfSettings.leftMargin;
        lineHeight+=2;
        defaultFontType = pdfSettings.getCommandFont();
        fontSize = pdfSettings.getCommandFontSize();
        textSplitting(lines);
    }

    public PDFAnswer(List<String> lines, int textWidth, FontType font, int fontSize, int leftMargin) throws IOException {
        super();
        this.maxTextWidth = textWidth;
        this.defaultFontType = font;
        this.leftMargin = leftMargin;
        this.fontSize = fontSize;
        lineHeight+=2;
    }
    
    @Override
    public void textSplitting (List<String> lines) throws IOException {
        float actualLineWidth;
        boolean alert = false;
        pdfLines.clear();
        
        for (String line : lines) {
            actualLineWidth = getWidth(line, defaultFontType, fontSize);
            
            if (actualLineWidth <= maxTextWidth) {
                PDFLine pdfLine = new PDFLine(fontSize, leftMargin);
                PDFLinePart lp = new PDFLinePart(defaultFontType);
                lp.setText(line);
                pdfLine.setLineParts(Arrays.asList(lp));
                pdfLines.add(pdfLine);
            }
            else {
                if (!alert) {
                    Alerts.tooNarrowPlaceForAnswer();
                    alert = true;
                }
            }
        }
    }
    
    public void setAnswers(List<String> answers) {
        int placeLength = 0;
        float underscore = 0, maxLength = 0;

        /* pobranie szerokości podkreślnika w pikselach */
        try {
            underscore = getWidth("_", defaultFontType,fontSize);
        } catch (IOException e) {
            System.out.println("Problem z pobraniem szerokości znaku");
        }

        /* ustalenie maksymalnej szerokości podkreślnika */
        maxLength = (float)maxTextWidth/underscore;

        for (String a : answers) {
            if (placeLength < a.length()) {
                placeLength = a.length();
            }
        }

        for (int i = 0; i < placeLength && i < (int)maxLength; i++) {
            placeForAnswer += "_";
        }

        answers.stream().forEach((_item) -> {
            placesForAnswers.add(placeForAnswer);
        });
    }
    
    @Override
    public int writeToPDF(int y, boolean lineNumbersVisibility) throws IOException, EmptyPartOfTaskException {
        int answerIndex = 0;
        for (PDFLine pdfLine : pdfLines) {
            if (pdfLine.getLineParts().get(0).getText().contains("#placeForAnswer")) {
                pdfLine.getLineParts().get(0).setText(pdfLine.getLineParts().get(0).getText() + ' ');
                String[] list = pdfLine.getLineParts().get(0).getText().split("#placeForAnswer");
                String line = "";

                for (int j = 0; j < list.length - 1; j++) {
                    line += list[j] + placesForAnswers.get(answerIndex++);
                }
                pdfLine.getLineParts().get(0).setText(line + list[list.length - 1]);
            }
        }
        return super.writeToPDF(y, lineNumbersVisibility);
    }
}
