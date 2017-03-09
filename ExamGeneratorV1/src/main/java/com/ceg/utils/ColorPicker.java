package com.ceg.utils;

import java.awt.Color;


/**
 *
 * @author Martyna
 */
public enum ColorPicker {
    RED("CZERWONY", Color.RED),
    GREEN("ZIELONY", Color.GREEN),
    BLUE("NIEBIESKI", Color.BLUE),
    BLACK("CZARNY", Color.BLACK);
    
    private final String colorName;
    private final Color color;
    
    ColorPicker(String colorName, Color color) {
        this.colorName = colorName;
        this.color = color;
    }
    
    public String getColorName() {
        return colorName;
    }
    
    public Color getColor() {
        return color;
    }
}
