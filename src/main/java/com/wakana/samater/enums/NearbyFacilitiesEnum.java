package com.wakana.samater.enums;



public enum NearbyFacilitiesEnum {
    TATA("TATA"),
    DDD("DDD"),
    TAXI("TAXI"),
    CLANDO("CLANDO"),
    SHOPS("SHOPS"),
    MARKET("MARKET"),
    PLACE_TO_VISIT("PLACE_TO_VISIT"),
    SCHOOL("SCHOOL"),
    PHARMACY("PHARMACY");

    private final String value;

    NearbyFacilitiesEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
