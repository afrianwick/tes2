package com.pertamina.portal.core.interfaces;

public interface ApiParser {
    void parseXml(String xml, OnSuccessListener listener);
    void buildAlert();
}
