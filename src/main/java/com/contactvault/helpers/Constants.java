package com.contactvault.helpers;

public enum Constants {

    SCM,
    ROLE_USER,
    CONTACT_IMAGE_WIDTH(500),
    CONTACT_IMAGE_HEIGHT(500),
    CONTACT_IMAGE_CROP("fill"),
    PAGE_SIZE("10");

    private Integer intValue;
    private String stringValue;


    Constants(){
    }
    Constants(int value) {
        this.intValue = value;
    }

    Constants(String value) {
        this.stringValue = value;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

}
