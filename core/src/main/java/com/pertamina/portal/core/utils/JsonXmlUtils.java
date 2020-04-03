package com.pertamina.portal.core.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class JsonXmlUtils {

    public static JsonNode xmlToJsonNode(String xml) throws IOException {
        ObjectMapper jsonMapper = new ObjectMapper();
        return jsonMapper.readTree(xmlToJsonStr(xml));
    }

    public static String xmlToJsonStr(String xml) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        JsonNode node = xmlMapper.readTree(xml.getBytes());
        ObjectMapper jsonMapper = new ObjectMapper();
        String json = jsonMapper.writeValueAsString(node);

        return json;
    }

    public static String docDomToString(Document newDoc) throws Exception{
        DOMSource domSource = new DOMSource(newDoc);
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        StringWriter sw = new StringWriter();
        StreamResult sr = new StreamResult(sw);
        transformer.transform(domSource, sr);
        return sw.toString();
    }

    public static String nodeToString(Node node) throws TransformerException {
        StringWriter writer = new StringWriter();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.transform(new DOMSource(node), new StreamResult(writer));
        String xml = writer.toString();

        return xml;
    }

    public static JSONObject xmlNodeToJsonNode(Node node) throws TransformerException, IOException, JSONException {
        String strXml = nodeToString(node);
        String strJson = xmlToJsonStr(strXml);
        JSONObject jsonObject = new JSONObject(strJson);

        return jsonObject;
    }
}
