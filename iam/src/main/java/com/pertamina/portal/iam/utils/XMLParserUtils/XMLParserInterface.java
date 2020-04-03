package com.pertamina.portal.iam.utils.XMLParserUtils;

import org.w3c.dom.NodeList;

public interface XMLParserInterface {

    void onSuccess(String result);
    void onFailure(NodeList nodeListError);
    void onSuccessReturnMessage(NodeList nodeListError);
}
