package com.ceg.examContent;

import com.ceg.utils.Alerts;
import org.fxmisc.richtext.CodeArea;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Klasa reprezentująca tekst zawarty w polu CodeArea.
 */
public class Text {

    private List<TextPart> textParts;

    public Text() {
        textParts = new ArrayList();
    }

    @XmlElementWrapper(name = "textParts")
    @XmlElement(name = "textPart")
    public List<TextPart> getTextParts() {
        return textParts;
    }
    public void setTextParts(List<TextPart> textParts) {
        this.textParts = textParts;
    }

    /**
     * Wydobywa kod z pola CodeArea i przypisuje go wraz ze stanem znaczników do obiektu klasy Text.
     * @param code Pole CodeArea z którego ma zostać wydobyty kod wraz ze znacznikami.
     */
    public void extractText(CodeArea code) {
        textParts.clear();
        int length = code.getLength();
        String currentStyle = "[]";
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < length; i++) {
            if(code.getStyleOfChar(i).toString().equals(currentStyle)) {
                sb.append(code.getText().charAt(i));
            }
            else {
                if(i != 0) {
                    TextPart part = new TextPart();
                    part.setText(sb.toString());
                    sb.setLength(0);
                    part.setType(currentStyle);
                    textParts.add(part);
                }
                sb.append(code.getText().charAt(i));
                currentStyle = code.getStyleOfChar(i).toString();
            }
        }
        TextPart part = new TextPart();
        part.setText(sb.toString());
        sb.setLength(0);
        part.setType(currentStyle);
        textParts.add(part);
    }

    /**
     * Wpisuje do pola CodeArea zawartość obiektu klasy Text dla danego zadania.
     * @param code Pole CodeArea które ma zostać wypełnione zawartością obiektu klasy Text.
     */
    public void createCodeAreaText(CodeArea code) {
        code.clear();
        StringBuilder sb = new StringBuilder();
        for(TextPart part : textParts) {
            sb.append(part.getText());
        }
        code.appendText(sb.toString());
        int start;
        start = 0;
        for(TextPart part : textParts) {
            code.setStyleClass(start, start + part.getText().length(), part.getType().substring(1, part.getType().length()-1));
            start += part.getText().length();
        }
    }

    /**
     * Wydobywa z obiektu klasy Text kod używany podczas standardowej kompilacji.
     * @return Lista linii z kodem który ma zostać skompilowany.
     */
    public List<String> getStandardCompilationCode() {
        List<TextPart> code = new ArrayList<TextPart>(textParts.size());

        for (TextPart part : textParts) {
            try {
                code.add((TextPart)part.clone());
            } catch (CloneNotSupportedException e) {
                Alerts.getCodeToCompileErrorAlert();
            }
        }

        for(int i = 0; i < code.size(); i++) {
            if(code.get(i).getType().equals("[test]")) {
                code.remove(i);
                i--;
            }
        }
        return convertList(code);
    }

    /**
     * Wydobywa z obiektu klasy Text kod używany podczas tworzenia pliku .pdf.
     * @return Lista linii z kodem który ma się znaleźć w pliku .pdf.
     */
    public List<String> getPDFCode() {

        List<TextPart> code = new ArrayList<TextPart>(textParts.size());

        for (TextPart part : textParts) {
            try {
                code.add((TextPart)part.clone());
            } catch (CloneNotSupportedException e) {
                Alerts.getCodeToPdfErrorAlert();
            }
        }

        for(int i = 0; i < code.size(); i++) {
            if(code.get(i).getType().equals("[test]") || code.get(i).getType().equals("[hidden]")) {
                code.remove(i);
                i--;
            }
            else if(code.get(i).getType().equals("[gap]")) {
                if(code.get(i).getText().substring(code.get(i).getText().length() - 1).equals("\n"))
                    code.get(i).setText("#placeForAnswer\n");
                else
                    code.get(i).setText("#placeForAnswer");
            }
        }
        return convertList(code);
    }

    /**
     * Wydobywa z obiektu klasy Text kod używany podczas kompilacji testowej (pełen).
     * @return Lista linii z kodem który ma zostać skompilowany.
     */
    public List<String> getGUIText() {
        List<TextPart> code = new ArrayList(textParts);
        return convertList(code);
    }

    /**
     * Przekształca listę obiektów typu TextPart na listę obiektów typu String.
     * @param input Wejściowa lista obiektów klasy TextPart.
     * @return Lista linii zawierająca wartości tekstowe podanych obiektów klasy TextPart.
     */
    public List<String> convertList(List<TextPart> input) {
        StringBuilder sb = new StringBuilder();
        for(TextPart part : input) {
            sb.append(part.getText());
        }

        List<String> output = new ArrayList();
        String[] list = sb.toString().split("\n");
        Collections.addAll(output, list);

        return output;
    }
}