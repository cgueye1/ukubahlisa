package com.wakana.realestateworks.enums;

public enum ProfilEnum {
    PROMOTEUR("PROMOTEUR"),
    NOTAIRE("NOTAIRE"),
    RESERVATAIRE("RESERVATAIRE"),
    BANK("BANK"),
    AGENCY("AGENCY"),
    ADMIN("ADMIN"),

    PROPRIETAIRE("PROPRIETAIRE"),
    SYNDIC("SYNDIC"),
    LOCATAIRE("LOCATAIRE"),
    PRESTATAIRE("PRESTATAIRE"),
    TOM("TOM"),

    SITE_MANAGER("SITE_MANAGER"),
    SUPPLIER("SUPPLIER"),
    SUBCONTRACTOR("SUBCONTRACTOR"),
    WORKER("WORKER"),
    MOA("MOA"),
    BET("BET");

    private final String value;

    ProfilEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
