package com.wakana.realestateworks.enums;


public enum ConstructionPhaseName {
    GROS_OEUVRE("Gros œuvre"),
    SECOND_OEUVRE("Second œuvre"),
    FINITION("Finition");

    private final String label;

    ConstructionPhaseName(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
