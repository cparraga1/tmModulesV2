package com.tmModulos.controlador.utils;

/**
 * Created by user on 04/10/2017.
 */
public class ListObject {

    private String label;
    private String value;

    public ListObject() {
    }

    public ListObject(String label, String value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
