package com.spaceotechnologies.training.stopwatch.data;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Kostez on 25.08.2016.
 */

public class ColorTable {

    public static final String ID_FIELD = "color_id";
    public static final String NAME_FIELD = "color_name";
    public static final String VALUE_FIELD = "color_value";

    @DatabaseField(generatedId = true, columnName = ID_FIELD)
    private int colorId;

    @DatabaseField(columnName = NAME_FIELD)
    private String colorName;

    @DatabaseField(columnName = VALUE_FIELD)
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
