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

import com.example.library.converter.FromXmlToLetterConverter;
import com.example.library.logger.Logger;
import com.example.library.model.entity.Letter;

public class LetterClientREST extends ClientREST {

    public static final String URL_LETTERS_LIST = "/web-service/letters.xml"; //"/web-service/letters/get-list";
    public static final String BY_BOOK = "BOOK";
    public static final String BY_CATEGORY = "CATEGORY";
    public static final String BY_WRITER = "WRITER";
    protected final static String LOG_TAG = LetterClientREST.class.getName();

    public Map<String, Object> getLetters(String by) {
        return parseResponseList(super.call(URL_LETTERS_LIST, METHOD_GET, constructLettersListXml(by)));
    }

    private Document constructLettersListXml(String by) {
        Document document =  DocumentHelper.createDocument();
        Element root = document.addElement("request");
        constructConfig(root, null); 
        Element letters = root.addElement("letters");
        letters.addElement("by").addText(by);
        return document;
    }

    private Map<String, Object> parseResponseList(String response) {
        if(response == null || response.trim().equals("")) return null;
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            Document document = DocumentHelper.parseText(response);
            Map<String, String> config = getConfiguration(document.selectSingleNode(XPATH_CONFIG));
            Map<String, String> state = getResponseState(document.selectSingleNode(XPATH_STATE));
            List<Node> letters = document.selectNodes("/response/letters/letter");
            List<Letter> lettersList = new ArrayList<Letter>();
            for (Iterator<Node> iterator = letters.iterator(); iterator.hasNext();) {
                Node node = (Node) iterator.next();
                lettersList.add(FromXmlToLetterConverter.convert(node.selectSingleNode("."), config));		
            }
            result.put(KEY_CONFIG, config);
            result.put(KEY_STATE, state);
            result.put("letters", lettersList);
            return result;
        } catch (DocumentException e) {
            Logger.addMessage(LOG_TAG, "An error occured on parsing letter on letterClientREST : " + e.getMessage());
            Log.e(LOG_TAG, "An error occured on generating Document from " + response, e);
        }
        return null;
    }
	
}