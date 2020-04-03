package com.pertamina.portal.iam.utils.XMLParserUtils;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLParserUtils {

    public static void parseXml(String strXml, XMLParserInterface listener){
        DocumentBuilderFactory factory;
        DocumentBuilder builder;
        Document dom;

        try {
            InputStream is = new ByteArrayInputStream(strXml.getBytes("utf-8"));
            factory = DocumentBuilderFactory.newInstance();
            builder = factory.newDocumentBuilder();
            dom = builder.parse(is);

            NodeList nodeListError = dom.getElementsByTagName("ReturnMessage");
            NodeList returnType = dom.getElementsByTagName("ReturnType");
            String strReturnType = returnType.item(0).getTextContent();

            listener.onSuccessReturnMessage(nodeListError);

            if (strReturnType.equalsIgnoreCase("S")) {
                NodeList nodeListSuccess = dom.getElementsByTagName("ReturnObject");

                if (nodeListSuccess.getLength() > 0) {
                    listener.onSuccess(nodeListSuccess.item(0).getTextContent());
                }
            } else {
                for (int i = 0; i < nodeListError.getLength(); i++) {
//                    String strError = nodeListError.item(i).getTextContent();
//                    String message = "Could not get data due to:" + strError;
//
//                    if (strError.contains("401")) {
//                        message = "Could not get data. It might be you are Loged in from other device or your session was exiperd.";
//                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Login again", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                try {
//                                    startActivity(new Intent(ReqSuratVisiActivity.this,
//                                            Class.forName("com.pertamina.portal.activity.LoginActivity")));
//                                    finish();
//                                } catch (ClassNotFoundException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                    }
//
//                    alertDialog.setMessage(message);
//
//                    if (!alertDialog.isShowing()) {
//                        alertDialog.show();
//                    }
                    listener.onFailure(nodeListError);
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
