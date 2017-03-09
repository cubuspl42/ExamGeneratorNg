package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import com.ceg.utils.ColorPicker;
import com.ceg.utils.ContentCssClass;
import com.ceg.utils.FontType;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


public class PDFTeachersAnswer extends PDFAnswer {
    //pole przechowujace listę odpowiedzi do zadania
    protected List<String> answers;
    protected int teacherAnswerFontSize;
    protected FontType teacherAnswerFontType;
    protected ColorPicker answerColor;
    
    //pole przechowujące informację, który indeks z listy odpowiedzi będzie wykorzystany w przypadku wystąpienia 
    //następnej luki
    private int answersIndex;

    public PDFTeachersAnswer(List<String> lines, float codeWidthPercentage) throws IOException {
        super(lines, codeWidthPercentage);
        answersIndex = 0;
        teacherAnswerFontSize = PDFSettings.getInstance().getAnswerFontSize();
        teacherAnswerFontType = PDFSettings.getInstance().getAnswerFont();
        answerColor = PDFSettings.getInstance().getAnswerColor();
        lineHeight = teacherAnswerFontSize + 3;
        
        setFontTypeBoldOrItalic();
    }

    public PDFTeachersAnswer(List<String> lines, int textWidth, FontType font, int fontSize, int leftMargin) throws IOException {
        super(lines, textWidth, font, fontSize, leftMargin);
        answersIndex = 0;
        teacherAnswerFontSize = PDFSettings.getInstance().getAnswerFontSize();
        teacherAnswerFontType = PDFSettings.getInstance().getAnswerFont();
        answerColor = PDFSettings.getInstance().getAnswerColor();
        lineHeight = teacherAnswerFontSize + 3;
        
        setFontTypeBoldOrItalic();
    }
    
    /* funkcja zapisująca do dokumentu pdf najpierw odpowiedzi dla nauczyciela a następnie całą treść zadania */
    @Override
    public int writeToPDF(int y, boolean lineNumbersVisibility) throws IOException, EmptyPartOfTaskException {
        if (answers.isEmpty())
            throw new EmptyPartOfTaskException();
        answersIndex = 0;
        
        if (answers != null) {
            float answerWidth;
            int myY = y;
            for (PDFLine i : pdfLines) {
                String[] answersPlaces;
                if (i.getLineParts().get(0).getText().contains("#placeForAnswer")) {
                    i.getLineParts().get(0).setText(i.getLineParts().get(0).getText() + ' ');
                    answersPlaces = i.getLineParts().get(0).getText().split("#placeForAnswer"); 
                    float lineWidth = leftMargin;

                    for (int j = 0; j < answersPlaces.length - 1; j++) {
                        answerWidth = getWidth(answersPlaces[j], defaultFontType, fontSize);
                        lineWidth += answerWidth + 1;
                        PDFLine pdfLine = new PDFLine(teacherAnswerFontSize, (int)lineWidth);
                        PDFLinePart linePart = new PDFLinePart(teacherAnswerFontType);
                        linePart.setText(answers.get(answersIndex));
                        lineWidth += getWidth(placesForAnswers.get(answersIndex++), defaultFontType, fontSize);
                        pdfLine.setLineParts(Arrays.asList(linePart));
                        pdfLine.writeLineInColor(myY, answerColor.getColor());
                    }
                }
                myY -= lineHeight;
            }            
        }
        return super.writeToPDF(y, lineNumbersVisibility);
    }
    
    @Override
    public void setAnswers(List<String> answers) {
        this.answers = answers;
        super.setAnswers(answers);
    }
    
    private void setFontTypeBoldOrItalic() {
        if (PDFSettings.getInstance().getIsAnswerBold() && PDFSettings.getInstance().getIsAnswerItalic()) {
            teacherAnswerFontType = teacherAnswerFontType.contentCssClassToFontType(ContentCssClass.BOLD_ITALIC);
        }
        else if (PDFSettings.getInstance().getIsAnswerBold()) {
            teacherAnswerFontType = teacherAnswerFontType.contentCssClassToFontType(ContentCssClass.BOLD);
        }
        else if (PDFSettings.getInstance().getIsAnswerItalic()) {
            teacherAnswerFontType = teacherAnswerFontType.contentCssClassToFontType(ContentCssClass.ITALIC);
        }
    }
}
