package com.pertamina.portal.core.models.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "ReturnVariable")
public class K2Service {

    @Element(name = "ReturnObject")
    private long returnObject;

    @Element(name = "ReturnType")
    private String returnType;

    @Element(name = "ReturnMessage")
    private String returnMessage;

    public long getReturnObject() {
        return returnObject;
    }

    public void setReturnObject(long returnObject) {
        this.returnObject = returnObject;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }
}
