package com.wakana.realestateworks.enums;

public  enum ReservationStatus {
    PROSPECT("PROSPECT"),
    RESERVED("RESERVED");

    private final String value;

    ReservationStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
