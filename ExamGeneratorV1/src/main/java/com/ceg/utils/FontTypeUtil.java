package com.ceg.utils;

import static com.ceg.utils.ContentCssClass.*;
import java.util.ArrayList;
import java.util.List;


public final class FontTypeUtil {
    public static final FontType change(String fontName) {
        for (FontType ft : FontType.values()) {
            if (fontName.equals(ft.getFontName()))
                return ft;
        }
        return null;
    }
    
    public static final List<String> getFontNamesList() {
        List<String> result = new ArrayList<>();
        
        for (FontType ft : FontType.values()) {
            if (ft.getFontName() != null)
                result.add(ft.getFontName());
        }
        
        return result;
    }
    
    public static final ContentCssClass stringToContentCssClass(String name) {
        switch (name) {
            case "[]":
                return EMPTY;
            case "[bold]":
                return BOLD;
            case "[italic]":
                return ITALIC;
            case "[underline]":
                return UNDERLINE;
            case "[bolditalic]":
                return BOLD_ITALIC;
            case "[boldunderline]":
                return BOLD_UNDERLINE;
            case "[italicunderline]":
                return ITALIC_UNDERLINE;
            case "[bolditalicunderline]":
                return BOLD_ITALIC_UNDERLINE;
            case "[monospace]":
                return MONOSPACE;
            case "[monospacebold]":
                return MONOSPACE_BOLD;
            case "[monospaceitalic]":
                return MONOSPACE_ITALIC;
            case "[monospaceunderline]":
                return MONOSPACE_UNDERLINE;
            case "[monospacebolditalic]":
                return MONOSPACE_BOLD_ITALIC;
            case "[monospaceboldunderline]":
                return MONOSPACE_BOLD_UNDERLINE;
            case "[monospaceitalicunderline]":
                return MONOSPACE_ITALIC_UNDERLINE;
            case "[monospacebolditalicunderline]":
                return MONOSPACE_BOLD_ITALIC_UNDERLINE;
            case "[undo]":
                return UNDO;
            default:
                return null;
        }
    }
}
