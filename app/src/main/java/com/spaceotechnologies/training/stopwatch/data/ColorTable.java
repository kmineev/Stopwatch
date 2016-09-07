package com.spaceotechnologies.training.stopwatch.data;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Kostez on 25.08.2016.
 */

public class ColorTable {

    public static final String NAME_FIELD = "color_name";

    @DatabaseField(generatedId = true, columnName = "color_id")
    private int colorId;

    @DatabaseField(columnName = NAME_FIELD)
    private String colorName;

    @DatabaseField(columnName = "color_value")
    private String colorValue;

    @DatabaseField(columnName = "is_background_image")
    private boolean isBackgroundImage;

    @DatabaseField(columnName = "background_image_file_name")
    private String backgroundImageFileName;

    public ColorTable() {

    }

    public ColorTable(String colorName, String colorValue) {
        this.colorName = colorName;
        this.colorValue = colorValue;
    }

    public int getColorId() {
        return colorId;
    }

    public String getColorName() {
        return colorName;
    }

    public String getColorValue() {
        return colorValue;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public void setColorValue(String colorValue) {
        this.colorValue = colorValue;
    }

    public boolean isBackgroundImage() {
        return isBackgroundImage;
    }

    public void setBackgroundImage(boolean backgroundImage) {
        isBackgroundImage = backgroundImage;
    }

    public String getBackgroundImageFileName() {
        return backgroundImageFileName;
    }

    public void setBackgroundImageFileName(String backgroundImageFileName) {
        this.backgroundImageFileName = backgroundImageFileName;
    }
}
