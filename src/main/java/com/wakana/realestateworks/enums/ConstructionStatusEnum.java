package com.wakana.realestateworks.enums;

public enum ConstructionStatusEnum {
    IN_PROGRESS("IN_PROGRESS"),
    DELAYED("DELAYED"),
    PENDING("PENDING"),
    COMPLETED("COMPLETED");

    private final String value;

    ConstructionStatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
