package com.ceg.pdf;

import com.ceg.exceptions.EmptyPartOfTaskException;
import com.ceg.utils.Alerts;
import com.ceg.utils.ColorPicker;
import com.ceg.utils.ColorPickerUtil;
import com.ceg.utils.FontType;
import com.ceg.utils.FontTypeUtil;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;


public class PDFSettings {
    private final Properties defaultSettings = new Properties();
    
    private FontType commandFont;
    private FontType codeFont;
    private FontType answerFont;
    private FontType titleFont;
    private FontType commentFont;
    private Integer commandFontSize;
    private Integer codeFontSize;
    private Integer answerFontSize;
    private Integer titleFontSize;
    private Integer commentFontSize;
    private String testType;
    private String pdfFilePath;
    private String pdfFileName;
    private ColorPicker answerColor;
    private ColorPicker titleColor;
    private ColorPicker commentColor;
    private boolean isAnswerBold;
    private boolean isAnswerItalic;
    private boolean isTitleBold;
    private boolean isTitleItalic;
    private boolean isCommentBold;
    private boolean isCommentItalic;
    
    private Integer year;
    private Integer month;
    private Integer day;
    
    private String date;
    private File pdfFile;

    public static int leftMargin;
    public static int rightMargin;
    public static int topMargin;
    public static int bottomMargin;
    public static int breakBetweenTasks;
    public static int pdfContentWidth;
    
    public static float separatorWidth;
    private boolean separatorsAfterTasks;

    private static final PDFSettings instance = new PDFSettings();

    public PDFSettings() {       
        preparePropertiesInput();
        
        leftMargin = Integer.parseInt(defaultSettings.getProperty("left.margin"));
        topMargin = Integer.parseInt(defaultSettings.getProperty("top.margin"));
        bottomMargin = Integer.parseInt(defaultSettings.getProperty("bottom.margin"));
        rightMargin = Integer.parseInt(defaultSettings.getProperty("right.margin"));
        breakBetweenTasks = Integer.parseInt(defaultSettings.getProperty("break.between.tasks")); 
        int breakBetweenTaskParts = Integer.parseInt(defaultSettings.getProperty("break.between.task.parts"));
        separatorWidth = Float.parseFloat(defaultSettings.getProperty("separator.width"));
        
        this.commandFont = FontTypeUtil.change(defaultSettings.getProperty("command.font"));
        this.codeFont = FontTypeUtil.change(defaultSettings.getProperty("code.font"));
        this.commandFontSize = Integer.parseInt(defaultSettings.getProperty("command.font.size"));
        this.codeFontSize = Integer.parseInt(defaultSettings.getProperty("code.font.size"));
        this.testType = defaultSettings.getProperty("test.type");
        this.pdfFileName = defaultSettings.getProperty("pdf.file.name");
        this.answerFont = commandFont;
        this.answerFontSize = commandFontSize;
        this.answerColor = ColorPickerUtil.change(defaultSettings.getProperty("answer.default.color"));
        this.titleFont = FontTypeUtil.change(defaultSettings.getProperty("title.font"));
        this.titleFontSize = Integer.parseInt(defaultSettings.getProperty("title.font.size"));;
        this.titleColor = ColorPickerUtil.change(defaultSettings.getProperty("title.default.color"));;
        this.commentFont = FontTypeUtil.change(defaultSettings.getProperty("comment.font"));
        this.commentFontSize = Integer.parseInt(defaultSettings.getProperty("comment.font.size"));;
        this.commentColor = ColorPickerUtil.change(defaultSettings.getProperty("comment.default.color"));;
        this.isAnswerBold = false;
        this.isAnswerItalic = false;
        this.isCommentBold = false;
        this.isCommentItalic = false;
        this.isTitleBold = false;
        this.isTitleItalic = false;
        
        Calendar calendar = Calendar.getInstance();        
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        
        File file = new File(".");
        pdfFilePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().length()-2);  
        pdfFile = null;
        pdfContentWidth = rightMargin - leftMargin - breakBetweenTaskParts;
        separatorsAfterTasks = false;
    }
    
    private void preparePropertiesInput() {
	String propFileName = "properties/pdfSettings.properties";
 
	InputStream inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
 
        try {
            if (inputStream != null) {
                defaultSettings.load(inputStream);
            }
            else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        } catch (Exception ex) {
            System.out.println("Cannot prepare properties for pdf settings. Error caused by: " + ex.toString());
        }
    }
    
    public void setCommandFont(FontType commandFont) {
        this.commandFont = commandFont;
    }
    public void setCodeFont(FontType codeFont) {
        this.codeFont = codeFont;
    }
    public void setAnswerFont(FontType codeFont) {
        this.answerFont = codeFont;
    }
    public void setAnswerColor(ColorPicker answerColor) {
        this.answerColor = answerColor;
    }
    public void setIsAnswerBold(boolean isAnswerBold) {
        this.isAnswerBold = isAnswerBold;
    }
    public void setIsAnswerItalic(boolean isAnswerItalic) {
        this.isAnswerItalic = isAnswerItalic;
    }
    public void setTitleFont(FontType titleFont) { this.titleFont = titleFont; }
    public void setTitleColor(ColorPicker titleColor) { this.titleColor = titleColor; }
    public void setIsTitleBold(boolean isTitleBold) {
        this.isTitleBold = isTitleBold;
    }
    public void setIsTitleItalic(boolean isTitleItalic) {
        this.isTitleItalic = isTitleItalic;
    }
    public void setCommentFont(FontType commentFont) { this.commentFont = commentFont; }
    public void setCommentColor(ColorPicker commentColor) { this.commentColor = commentColor; }
    public void setIsCommentBold(boolean isCommentBold) { this.isCommentBold = isCommentBold; }
    public void setIsCommentItalic(boolean isCommentItalic) { this.isCommentItalic = isCommentItalic; }
    public void setCommandFontSize(Integer commandFontSize) {
        this.commandFontSize = commandFontSize;
    }
    public void setCodeFontSize(Integer codeFontSize) {
        this.codeFontSize = codeFontSize;
    }
    public void setAnswerFontSize(Integer codeFontSize) {
        this.answerFontSize = codeFontSize;
    }
    public void setTitleFontSize(Integer titleFontSize) { this.titleFontSize = titleFontSize; }
    public void setCommentFontSize(Integer commentFontSize) { this.commentFontSize = commentFontSize; }
    public void setTestType(String testType) {
        this.testType = testType;
    }
    public void setPdfFilePath(String pdfFilePath) {
        this.pdfFilePath = pdfFilePath;
    }
    public void setPdfFileName(String pdfFileName) {
        this.pdfFileName = pdfFileName;
    }
    
    public void setYear(Integer year) {
        this.year = year;
    }
    public void setMonth(Integer month) {
        this.month = month;
    }
    public void setDay(Integer day) {
        this.day = day;
    }
    public void setSeparatorsAfterTasks(boolean separatorsAfterTasks) {
        this.separatorsAfterTasks = separatorsAfterTasks;
    }
    
    public FontType getCommandFont() {
        return commandFont;
    }
    public FontType getCodeFont() {
        return codeFont;
    }
    public FontType getAnswerFont() {
        return answerFont;
    }
    public FontType getTitleFont() {
        return titleFont;
    }
    public FontType getCommentFont() {
        return commentFont;
    }
    public ColorPicker getAnswerColor() {
        return answerColor;
    }
    public ColorPicker getTitleColor() {
        return titleColor;
    }
    public ColorPicker getCommentColor() {
        return commentColor;
    }
    public boolean getIsAnswerBold() {
        return isAnswerBold;
    }
    public boolean getIsAnswerItalic() { return isAnswerItalic; }
    public boolean getIsTitleBold() {
        return isTitleBold;
    }
    public boolean getIsCommentBold() {
        return isCommentBold;
    }
    public boolean getIsTitleItalic() { return isTitleItalic; }
    public boolean getIsCommentItalic() { return isCommentItalic; }
    public Integer getCommandFontSize() {
        return commandFontSize;
    }
    public Integer getCodeFontSize() {
        return codeFontSize;
    }
    public Integer getAnswerFontSize() {
        return answerFontSize;
    }
    public Integer getCommentFontSize() {
        return commentFontSize;
    }
    public Integer getTitleFontSize() {
        return titleFontSize;
    }
    public String getTestType() {
        return testType;
    }
    public String getPdfFileName() {
        return pdfFileName;
    }
    public String getPdfFilePath() {
        return pdfFilePath;
    }
    
    public Integer getYear() {
        return year;
    }
    public Integer getMonth() {
        return month;
    }
    public Integer getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }
    
    public File getPdfFile() {
        return pdfFile;
    }
    public void setPdfFile(File pdfFile) { this.pdfFile = pdfFile; }
    
    public float getSeparatorWidth() {
        return separatorWidth;
    }
    
    public boolean getSeparatorsAfterTasks() {
        return separatorsAfterTasks;
    }

    public void formatDate() {
        date = "";
        
        if (day < 10)
            date = "0";
        date += day.toString() + '.';
        if (month < 10)
            date += '0';
        date += month.toString() + '.' + year.toString();
    }
    
    public void pdfGenerate(Stage stage) throws IOException {
        try {
            PDFGenerator gen = new PDFGenerator();
        } catch (IOException ex) {
            Alerts.fileAlreadyOpened();
            PDFGenerator.document.close();
        } catch (EmptyPartOfTaskException ex) {
            Alerts.emptyPartOfTaskAlert();
        }
    }

    public static PDFSettings getInstance() {
        return instance;
    }
}
