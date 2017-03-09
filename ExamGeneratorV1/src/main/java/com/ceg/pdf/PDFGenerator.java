package com.ceg.pdf;

import com.ceg.examContent.Exam;
import com.ceg.examContent.Task;
import com.ceg.exceptions.EmptyPartOfTaskException;
import com.ceg.gui.GUIMainController;
import com.ceg.utils.Alerts;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javafx.application.Platform;

/**
 * klasa odpowiedzialna za generowanie pdfa. Aby utworzyć dokument należy utworzyć obiekt tej klasy za pomocą konstruktora
 */
public class PDFGenerator {
    public static PDDocument document;
    private PDPage actualPage = null;
    public static PDPageContentStream cs;
    private static final int topMargin = PDFSettings.topMargin;
    private static final int bottomMargin = PDFSettings.bottomMargin;

    private PDFCommand comm;
    private PDFCode code;
    private PDFAnswer answer;

    private int actualY;
    
    /* wywołanie konstruktora powoduje utworzenie dokumentu pdf. Argumentem nazwa przyszłego pliku pdf */
    public PDFGenerator() throws IOException, EmptyPartOfTaskException {
        int breakBetweenTasks = PDFSettings.breakBetweenTasks;

        //tworzenie dokumentu, pierwszej strony i dodanie jej do dokumentu
        document = new PDDocument();
        createNewPage();
        
        PDFHeader header = new PDFHeader();
        actualY = header.setHeader(breakBetweenTasks);

        List<Task> taskList = Exam.getInstance().getTasks();
        
        Integer taskNumber = 1;
        for (Task i : taskList) {
            if (i.getText().getPDFCode().isEmpty() || i.getContent().getContentParts().size() <= 0 || i.getAnswers().size() < i.getPdfAnswers().size())
                throw new EmptyPartOfTaskException();
            
            comm = new PDFCommand(i.getContent(), taskNumber++);
            createCodeAndAnswer(i, i.getContent().getPdfWidthPercentage());

            answer.setAnswers(i.getAnswers());
            code.setAnswer(answer);
   
            boolean lastTask = (taskList.indexOf(i) == taskList.size() - 1);
            writeTaskToPdf(i.getType().getLineNumbersVisibility(), PDFSettings.getInstance().getSeparatorsAfterTasks(), lastTask);
            actualY -= breakBetweenTasks;
        }
        cs.close();
        savePDF(PDFSettings.getInstance().getPdfFile());
    }
    
    /* Funkcja tworząca dokument pdf */
    private void savePDF(File pdfFile) throws IOException {
        document.save(pdfFile);
        document.close();      
        Platform.runLater(() -> {
            GUIMainController.getInstance().openPdfItem.setDisable(false);
            Alerts.fileGenerated();
        });       
    }
    
    private void createNewPage() throws IOException {
        if (actualPage != null)
            cs.close();
        
        actualPage = new PDPage();
        document.addPage(actualPage);
        cs = new PDPageContentStream(document, actualPage);
        
        actualY = topMargin;
    }

    private void writeTaskToPdf(boolean lineNumbersVisibility, boolean separators, boolean lastTask) throws IOException, EmptyPartOfTaskException {
        if (actualY - comm.getLineHeight()*comm.getNumberOfLines() -
                answer.getLineHeight()*answer.getNumberOfLines() < bottomMargin  ||
                actualY - code.getLineHeight()*code.getNumberOfLines() < bottomMargin) {
            createNewPage();
        }

        int commandLines = comm.writeToPDF(actualY, lineNumbersVisibility);
        int codeLines = code.writeToPDF(actualY, lineNumbersVisibility);
        
        if (answer.getNumberOfLines() > 0)
            commandLines = answer.writeToPDF(commandLines, lineNumbersVisibility);

        actualY = (commandLines < codeLines) ? commandLines : codeLines;
        
        if (separators && !lastTask) {
            PDFGenerator.cs.moveTo(comm.leftMargin, actualY);
            PDFGenerator.cs.lineTo(code.leftMargin+code.maxTextWidth, actualY);
            PDFGenerator.cs.setLineWidth(PDFSettings.getInstance().getSeparatorWidth());
            PDFGenerator.cs.stroke();
        }
    }

    private void createCodeAndAnswer(Task task, float pdfContentWidthPercentage) throws IOException, EmptyPartOfTaskException {
        switch(PDFSettings.getInstance().getTestType()) {
            case "nauczyciel":
                if (task.getType().name.equals("Gaps")) {
                    code = new PDFTeachersGapsCode(task.getText().getPDFCode(), 1.0f - pdfContentWidthPercentage);
                    answer = code.answer;
                }
                else {
                    code = new PDFCode(task.getText().getPDFCode(), 1.0f - pdfContentWidthPercentage);
                    answer = new PDFTeachersAnswer(task.getPdfAnswers(), pdfContentWidthPercentage);
                }
                break;
            default:
                    switch (task.getType().name) {
                        case "Gaps":
                            code = new PDFGapsCode(task.getText().getPDFCode(), 1.0f - pdfContentWidthPercentage);
                            answer = code.answer;
                            break;
                        default:
                            code = new PDFCode(task.getText().getPDFCode(), 1.0f - pdfContentWidthPercentage);
                            answer = new PDFAnswer(task.getPdfAnswers(), pdfContentWidthPercentage);
                            break;
                    }
                break;
        }
        
    }
}
