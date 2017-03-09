package com.ceg.examContent;

/**
 * Klasa reprezentująca pojedynczy ciąg tekstu danego typu w polu CodeArea.
 */
public class TextPart {

    private String text;
    private String type;

    public TextPart() {

    }
    public TextPart(TextPart part) {
        this.text = part.getText();
        this.type = part.getType();
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public int lineCount(){
        return this.getText().split("\n").length;
    }
    /**
     * Wydobywa z obiektu klasy TextPart tekst, który przechowuje.
     * @return Tekst zawarty w obiekcie klasy TextPart, określony dla danego typu.
     */
    @Override
    public String toString() {
        return this.getText();
    }

    /**
     * Umożliwia klonowanie obiektów typu TextPart.
     * @return Sklonowany obiekt.
     * @throws CloneNotSupportedException
     */
    public Object clone() throws CloneNotSupportedException {
        return new TextPart(this);
    }
}
