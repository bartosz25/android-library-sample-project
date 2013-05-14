package com.example.library.rest;

import java.util.ArrayList;
import java.util.Date;
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
import com.example.library.converter.UniversalConverter;
import com.example.library.logger.Logger;
import com.example.library.model.entity.Book;
import com.example.library.model.entity.Borrowing;

public class LoggerClientREST extends ClientREST {
    public static final String URL_SEND_MESSAGES = "/web-service/log.xml";
    protected final static String LOG_TAG = LoggerClientREST.class.getName();

    public void sendMessages(Map<String, List<String>> messages) {
         super.call(URL_SEND_MESSAGES, METHOD_GET, constructSendXml(messages));
    }

    protected Document constructSendXml(Map<String, List<String>> messages) {
        Document document =  DocumentHelper.createDocument();
        Element root = document.addElement("request");
        constructConfig(root, null);
        Element errors = root.addElement("errors");
        for (Map.Entry<String, List<String>> entry : messages.entrySet()) {
            Element errorNode = errors.addElement(entry.getKey());
            for (String message : (List<String>) entry.getValue()) {
                Element errorMessage = errorNode.addElement("message").addText(message);	
            }
        }
        return document;
    }
}