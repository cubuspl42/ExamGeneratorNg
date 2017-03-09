package com.ceg.utils;

/**
 *
 * @author Martyna
 */
public class ColorPickerUtil {
    public static final ColorPicker change(String colorName) {
        for (ColorPicker cp : ColorPicker.values()) {
            if (colorName.equals(cp.getColorName()))
                return cp;
        }
        return null;
    }
}
