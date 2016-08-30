package com.spaceotechnologies.training.stopwatch.data;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Kostez on 25.08.2016.
 */

public class ColorTable {

    @DatabaseField(generatedId = true, columnName = "color_id")
    private int colorId;

    @DatabaseField(columnName = "color_name")
    private String colorName;

    @DatabaseField(columnName = "color_value")
    private String colorValue;

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
}
