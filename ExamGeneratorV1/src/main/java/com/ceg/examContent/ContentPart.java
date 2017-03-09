package com.ceg.examContent;

import com.ceg.utils.ContentCssClass;

/**
 *
 * @author Martyna
 */
public class ContentPart {
    private ContentCssClass cssClassName;
    private String text;

    public ContentPart() {

    }

    public ContentPart(ContentCssClass fontType, String text) {
        this.cssClassName = fontType;
        this.text = text;
    }
    
    public String getText() {
        return text;
    }
    
    public ContentCssClass getCssClassName() {
        return cssClassName;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCssClassName(ContentCssClass cssClassName) {
        this.cssClassName = cssClassName;
    }
}
