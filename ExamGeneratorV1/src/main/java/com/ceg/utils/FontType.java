package com.ceg.utils;


public enum FontType {
    
    MONO_BOLD("fonts/LiberationMono-Bold.ttf"),
    MONO_BOLD_ITALIC("fonts/LiberationMono-BoldItalic.ttf"),
    MONO_ITALIC("fonts/LiberationMono-Italic.ttf"),
    MONO_REGULAR("fonts/LiberationMono-Regular.ttf", "Liberation Mono"),
    
    SANS_BOLD("fonts/LiberationSans-Bold.ttf"),
    SANS_BOLD_ITALIC("fonts/LiberationSans-BoldItalic.ttf"),
    SANS_ITALIC("fonts/LiberationSans-Italic.ttf"),
    SANS_REGULAR("fonts/LiberationSans-Regular.ttf", "Liberation Sans"),
    
    SANS_NARROW_BOLD("fonts/LiberationSansNarrow-Bold.ttf"),
    SANS_NARROW_BOLD_ITALIC("fonts/LiberationSansNarrow-BoldItalic.ttf"),
    SANS_NARROW_ITALIC("fonts/LiberationSansNarrow-Italic.ttf"),
    SANS_NARROW_REGULAR("fonts/LiberationSansnarrow-Regular.ttf", "Liberation Sans Narrow"),
    
    SERIF_BOLD("fonts/LiberationSerif-Bold.ttf"),
    SERIF_BOLD_ITALIC("fonts/LiberationSerif-BoldItalic.ttf"),
    SERIF_ITALIC("fonts/LiberationSerif-Italic.ttf"),
    SERIF_REGULAR("fonts/LiberationSerif-Regular.ttf", "Liberation Serif");
    
    private final String fileName;
    private final String fontName;
    
    FontType() {
        this.fileName = null;
        this.fontName = null;
    }
    
    FontType(String fileName) {
        this.fileName = fileName;
        this.fontName = null;
    }
    
    FontType(String fileName, String fontName) {
        this.fileName = fileName;
        this.fontName = fontName;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public String getFontName() {
        return fontName;
    }
    
    public FontType contentCssClassToFontType(ContentCssClass cssClass) {
        switch (this) {
            case MONO_REGULAR:
                return changeMonoRegular(cssClass);
            case SANS_REGULAR:
                return changeSansRegular(cssClass);
            case SANS_NARROW_REGULAR:
                return changeSansNarrowRegular(cssClass);
            case SERIF_REGULAR:
                return changeSerifRegular(cssClass);
            default:
                return null;
        }
    }
    
    private FontType changeMonoRegular(ContentCssClass cssClass) {
        switch (cssClass) {
            case BOLD:
                return MONO_BOLD;
            case BOLD_UNDERLINE:
                return MONO_BOLD;
            case ITALIC:
                return MONO_ITALIC;
            case ITALIC_UNDERLINE:
                return MONO_ITALIC;
            case BOLD_ITALIC:
                return MONO_BOLD_ITALIC;
            case BOLD_ITALIC_UNDERLINE:
                return MONO_BOLD_ITALIC;
            default:
                return this;
        }
    }
    
    private FontType changeSansRegular(ContentCssClass cssClass) {
        switch (cssClass) {
            case BOLD:
                return SANS_BOLD;
            case BOLD_UNDERLINE:
                return SANS_BOLD;
            case ITALIC:
                return SANS_ITALIC;
            case ITALIC_UNDERLINE:
                return SANS_ITALIC;
            case BOLD_ITALIC:
                return SANS_BOLD_ITALIC;
            case BOLD_ITALIC_UNDERLINE:
                return SANS_BOLD_ITALIC;
            case MONOSPACE:
                return MONO_REGULAR;
            case MONOSPACE_UNDERLINE:
                return MONO_REGULAR;
            case MONOSPACE_BOLD:
                return MONO_BOLD;
            case MONOSPACE_BOLD_UNDERLINE:
                return MONO_BOLD;
            case MONOSPACE_ITALIC:
                return MONO_ITALIC;
            case MONOSPACE_ITALIC_UNDERLINE:
                return MONO_ITALIC;
            case MONOSPACE_BOLD_ITALIC:
                return MONO_BOLD_ITALIC;
            case MONOSPACE_BOLD_ITALIC_UNDERLINE:
                return MONO_BOLD_ITALIC;
            default:
                return this;
        }
    }
    
    private FontType changeSansNarrowRegular(ContentCssClass cssClass) {
        switch (cssClass) {
            case BOLD:
                return SANS_NARROW_BOLD;
            case BOLD_UNDERLINE:
                return SANS_NARROW_BOLD;
            case ITALIC:
                return SANS_NARROW_ITALIC;
            case ITALIC_UNDERLINE:
                return SANS_NARROW_ITALIC;
            case BOLD_ITALIC:
                return SANS_NARROW_BOLD_ITALIC;
            case BOLD_ITALIC_UNDERLINE:
                return SANS_NARROW_BOLD_ITALIC;
            case MONOSPACE:
                return MONO_REGULAR;
            case MONOSPACE_UNDERLINE:
                return MONO_REGULAR;
            case MONOSPACE_BOLD:
                return MONO_BOLD;
            case MONOSPACE_BOLD_UNDERLINE:
                return MONO_BOLD;
            case MONOSPACE_ITALIC:
                return MONO_ITALIC;
            case MONOSPACE_ITALIC_UNDERLINE:
                return MONO_ITALIC;
            case MONOSPACE_BOLD_ITALIC:
                return MONO_BOLD_ITALIC;
            case MONOSPACE_BOLD_ITALIC_UNDERLINE:
                return MONO_BOLD_ITALIC;
            default:
                return this;
        }
    }
    
    private FontType changeSerifRegular(ContentCssClass cssClass) {
        switch (cssClass) {
            case BOLD:
                return SERIF_BOLD;
            case BOLD_UNDERLINE:
                return SERIF_BOLD;
            case ITALIC:
                return SERIF_ITALIC;
            case ITALIC_UNDERLINE:
                return SERIF_ITALIC;
            case BOLD_ITALIC:
                return SERIF_BOLD_ITALIC;
            case BOLD_ITALIC_UNDERLINE:
                return SERIF_BOLD_ITALIC;
            case MONOSPACE:
                return MONO_REGULAR;
            case MONOSPACE_UNDERLINE:
                return MONO_REGULAR;
            case MONOSPACE_BOLD:
                return MONO_BOLD;
            case MONOSPACE_BOLD_UNDERLINE:
                return MONO_BOLD;
            case MONOSPACE_ITALIC:
                return MONO_ITALIC;
            case MONOSPACE_ITALIC_UNDERLINE:
                return MONO_ITALIC;
            case MONOSPACE_BOLD_ITALIC:
                return MONO_BOLD_ITALIC;
            case MONOSPACE_BOLD_ITALIC_UNDERLINE:
                return MONO_BOLD_ITALIC;
            default:
                return this;
        }
    }
}
