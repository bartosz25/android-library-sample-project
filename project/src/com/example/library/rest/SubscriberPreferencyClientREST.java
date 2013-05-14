package com.example.library.rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;

import android.util.Log;

import com.example.library.converter.FromXmlToBookConverter;
import com.example.library.converter.FromXmlToBorrowingConverter;
import com.example.library.model.entity.Book;

public class SubscriberPreferencyClientREST extends ClientREST {

    // TODO : voir si ces attributs doivent être publiques
    public static final String URL_PREFERENCIES_GET = "/web-service/preferencies.xml"; //"/web-service/subscriber/get-preferency";
    public static final String URL_PREFERENCIES_SET = "/web-service/preferencies.xml"; //"/web-service/subscriber/set-preferency";
    public static final String TYPE_ALL = "ALL";
    public static final String TYPE_WEB_SERVICE = "WEB_SERVICE";
    public static final String TYPE_WEBSITE = "WEBSITE";
    protected final static String LOG_TAG = SubscriberPreferencyClientREST.class.getName();

    public Map<String, Object> getPreferencies(String type) {
        return parseResponse(super.call(URL_PREFERENCIES_GET, METHOD_GET, constructPreferenciesGetXml(type)));
    }

    public Map<String, Object> postPreferencies(Map<String, String> preferencies) {
        return parseResponse(super.call(URL_PREFERENCIES_SET, METHOD_POST, constructPreferenciesSetXml(preferencies)));
    }

    public Map<String, Object> parseResponse(String response) {
        if(response == null || response.trim().equals("")) return null;
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            Document document = DocumentHelper.parseText(response);
            Log.d(LOG_TAG, "Received response " + response);
            Map<String, String> config = getConfiguration(document.selectSingleNode(XPATH_CONFIG));
            Map<String, String> state = getResponseState(document.selectSingleNode(XPATH_STATE));

            Map<String, String> preferenciesMap = new HashMap<String, String>(); 
            List<Node> preferencies = document.selectNodes("/response/user-preferencies/preferency");
            for(Iterator<Node> iterator = preferencies.iterator(); iterator.hasNext();) {
                Node node = (Node) iterator.next();

                Node code = node.selectSingleNode("./code");
                Node value = node.selectSingleNode("./value");
                if(code != null && value != null) {
                    preferenciesMap.put(code.getText(), value.getText());
                }
            }
            Log.d(LOG_TAG, "Found result " + preferenciesMap);
            result.put(KEY_CONFIG, config);
            result.put(KEY_STATE, state);
            result.put("preferencies", preferenciesMap);
            return result;
        } catch (DocumentException e) {
            Log.e(LOG_TAG, "An error occured on generating Document from " + response, e);
        }
        return null;
    }

    private Document constructPreferenciesGetXml(String type) {
        Document document =  DocumentHelper.createDocument();
        Element root = document.addElement("request");
        constructConfig(root, null);
        Element getPreferencies = root.addElement("get-preferencies");
        getPreferencies.addElement("type").addText(type);
        return document;
    }

    private Document constructPreferenciesSetXml(Map<String, String> preferencies) {
        Document document =  DocumentHelper.createDocument();
        Element root = document.addElement("request");
        constructConfig(root, null);
        Element setPreferencies = root.addElement("set-preferencies");
        for(Map.Entry<String, String> entry : preferencies.entrySet()) {
            Element preferency = setPreferencies.addElement("preferency");
            preferency.addElement("code").addText(entry.getKey());
            preferency.addElement("value").addText(entry.getValue());
        }
        return document;
    }

}