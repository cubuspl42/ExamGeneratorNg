package com.ceg.utils;


public enum ContentCssClass {
    EMPTY(""),
    BOLD("bold"),
    ITALIC("italic"),
    UNDERLINE("underline"),
    BOLD_ITALIC("bolditalic"),
    BOLD_UNDERLINE("boldunderline"),
    ITALIC_UNDERLINE("italicunderline"),
    BOLD_ITALIC_UNDERLINE("bolditalicunderline"),
    MONOSPACE("monospace"),
    MONOSPACE_BOLD("monospacebold"),
    MONOSPACE_ITALIC("monospaceitalic"),
    MONOSPACE_UNDERLINE("monospaceunderline"),
    MONOSPACE_BOLD_ITALIC("monospacebolditalic"),
    MONOSPACE_BOLD_UNDERLINE("monospaceboldunderline"),
    MONOSPACE_ITALIC_UNDERLINE("monospaceitalicunderline"),
    MONOSPACE_BOLD_ITALIC_UNDERLINE("monospacebolditalicunderline"),
    UNDO("undo");
    
    private final String className;
    ContentCssClass(String className) {
        this.className = className;
    }
    
    public final String getClassName() {
        return className;
    }
    
    public final String getClassList() {
        return '[' + className + ']';
    }
    
    public final ContentCssClass changeClass(String oldClass) {
        ContentCssClass old = FontTypeUtil.stringToContentCssClass(oldClass);
        if (old != null && old.getClassName().contains(this.getClassName())) {
            return old;
        }
        switch (this) {
            case BOLD:
                return boldNewClass(old);
            case ITALIC:
                return italicNewClass(old);
            case UNDERLINE:
                return underlineNewClass(old);
            case MONOSPACE:
                return monospaceNewClass(old);
            case UNDO:
                return this;
            default:
                return null;
        }
    }
    
    public final boolean isUnderlined() {
        return this == UNDERLINE || this == BOLD_UNDERLINE || this == ITALIC_UNDERLINE || 
                this == BOLD_ITALIC_UNDERLINE || this == MONOSPACE_UNDERLINE ||
                this == MONOSPACE_BOLD_UNDERLINE || this == MONOSPACE_ITALIC_UNDERLINE ||
                this == MONOSPACE_BOLD_ITALIC_UNDERLINE;
    }
    
    private ContentCssClass boldNewClass(ContentCssClass oldClass) {
        switch (oldClass) {
            case ITALIC:
                return BOLD_ITALIC;
            case UNDERLINE:
                return BOLD_UNDERLINE;
            case ITALIC_UNDERLINE:
                return BOLD_ITALIC_UNDERLINE;
            case MONOSPACE:
                return MONOSPACE_BOLD;
            case MONOSPACE_ITALIC:
                return MONOSPACE_BOLD_ITALIC;
            case MONOSPACE_UNDERLINE:
                return MONOSPACE_BOLD_UNDERLINE;
            case MONOSPACE_ITALIC_UNDERLINE:
                return MONOSPACE_BOLD_ITALIC_UNDERLINE;
            default:
                return BOLD;
        }
    }
    
    private ContentCssClass italicNewClass(ContentCssClass oldClass) {
        switch (oldClass) {
            case BOLD:
                return BOLD_ITALIC;
            case UNDERLINE:
                return ITALIC_UNDERLINE;
            case BOLD_UNDERLINE:
                return BOLD_ITALIC_UNDERLINE;
            case MONOSPACE:
                return MONOSPACE_ITALIC;
            case MONOSPACE_BOLD:
                return MONOSPACE_BOLD_ITALIC;
            case MONOSPACE_UNDERLINE:
                return MONOSPACE_ITALIC_UNDERLINE;
            case MONOSPACE_BOLD_UNDERLINE:
                return MONOSPACE_BOLD_ITALIC_UNDERLINE;
            default:
                return ITALIC;
        }
    }
    
    private ContentCssClass underlineNewClass(ContentCssClass oldClass) {
        switch (oldClass) {
            case BOLD:
                return BOLD_UNDERLINE;
            case ITALIC:
                return ITALIC_UNDERLINE;
            case BOLD_ITALIC:
                return BOLD_ITALIC_UNDERLINE;
            case MONOSPACE:
                return MONOSPACE_UNDERLINE;
            case MONOSPACE_BOLD:
                return MONOSPACE_BOLD_UNDERLINE;
            case MONOSPACE_ITALIC:
                return MONOSPACE_ITALIC_UNDERLINE;
            case MONOSPACE_BOLD_ITALIC:
                return MONOSPACE_BOLD_ITALIC_UNDERLINE;
            default:
                return UNDERLINE;
        }
    }
    
    private ContentCssClass monospaceNewClass(ContentCssClass oldClass) {
        switch (oldClass) {
            case BOLD:
                return MONOSPACE_BOLD;
            case ITALIC:
                return MONOSPACE_ITALIC;
            case UNDERLINE:
                return MONOSPACE_UNDERLINE;
            case BOLD_ITALIC:
                return MONOSPACE_BOLD_ITALIC;
            case BOLD_UNDERLINE:
                return MONOSPACE_BOLD_UNDERLINE;
            case ITALIC_UNDERLINE:
                return MONOSPACE_ITALIC_UNDERLINE;
            case BOLD_ITALIC_UNDERLINE:
                return MONOSPACE_BOLD_ITALIC_UNDERLINE;
            default:
                return MONOSPACE;
        }
    }
}
