
package com.wakana.realestateworks.enums;

public enum SubscriptionPlanEnum {
    PROMOTEUR(" PROMOTEUR"),
     SITE_MANAGER("SITE_MANAGER"),
    SUPPLIER("SUPPLIER"),
    SUBCONTRACTOR("SUBCONTRACTOR"),
    WORKER("WORKER"),
    MOA("MOA"),
    BET("BET")
    
    ;
    
    
    


    private final String value;

    SubscriptionPlanEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
