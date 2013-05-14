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

public class BorrowingClientREST extends ClientREST {

    public static final String URL_BORROWING = "/web-service/borrowing.xml"; //"/web-service/borrowing/get-per-user";
    public static final String URL_BORROW = "/web-service/borrow.xml"; //"/web-service/borrowing/borrow";
    public static final String TYPE_ALL = "ALL";
    public static final String TYPE_TO_RETURN = "TO_RETURN";
    protected final static String LOG_TAG = BorrowingClientREST.class.getName();

    public Map<String, Object> getBorrowings(String type) {
        return parseResponseList(super.call(URL_BORROWING, METHOD_GET, constructBorrowingsXml(type)));
    }

    public Map<String, Object> borrow(long bookId, Date date) {
        return parseBorrowResponse(super.call(URL_BORROW, METHOD_GET, constructBorrowXml(bookId, date)));
    }

    public Map<String, Object> parseResponseList(String response) {
        if(response == null || response.trim().equals("")) return null;
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            Document document = DocumentHelper.parseText(response);
            Map<String, String> config = getConfiguration(document.selectSingleNode(XPATH_CONFIG));
            Map<String, String> state = getResponseState(document.selectSingleNode(XPATH_STATE));

            List<Borrowing> borrowingsList = new ArrayList<Borrowing>();
            List<Node> borrowings = document.selectNodes("/response/borrowing/item");
            for(Iterator<Node> iterator = borrowings.iterator(); iterator.hasNext();) {
                Node node = (Node) iterator.next();
                borrowingsList.add(FromXmlToBorrowingConverter.convert(node.selectSingleNode("."), config));
            }
            Log.d(LOG_TAG, "Found books list " + borrowingsList);
            result.put(KEY_CONFIG, config);
            result.put(KEY_STATE, state);
            result.put("borrowings", borrowingsList);
            return result;
        } catch (DocumentException e) {
            Logger.addMessage(LOG_TAG, "An error occured on generatind document to borrowing request : " + e.getMessage());
            Log.e(LOG_TAG, "An error occured on generating Document from " + response, e);
        }
        return null;
    }
    public Map<String, Object> parseBorrowResponse(String response) {
        if(response == null || response.trim().equals("")) return null;
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            Document document = DocumentHelper.parseText(response);
            Map<String, String> config = getConfiguration(document.selectSingleNode(XPATH_CONFIG));
            Map<String, String> state = getResponseState(document.selectSingleNode(XPATH_STATE));

            Borrowing borrowing = FromXmlToBorrowingConverter.convert(document.selectSingleNode("/response/borrowing/item"), config);
            result.put(KEY_CONFIG, config);
            result.put(KEY_STATE, state);
            result.put("borrowing", borrowing);
            return result;
        } catch (DocumentException e) {
            Logger.addMessage(LOG_TAG, "An error occured on parsing borrowing response : " + e.getMessage());
            Log.e(LOG_TAG, "An error occured on generating Document from " + response, e);
        }
        return null;
    }

    protected Document constructBorrowingsXml(String type) {
        Document document =  DocumentHelper.createDocument();
        Element root = document.addElement("request");
        constructConfig(root, null);
        Element borrowing = root.addElement("borrowing");
        borrowing.addElement("type").addText(type);
        return document;
    }

    protected Document constructBorrowXml(long id, Date date) {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("request");
        constructConfig(root, null);
        Element borrowing = root.addElement("borrowing");
        borrowing.addElement("bookId").addText(""+id);
        borrowing.addElement("date").addText(UniversalConverter.fromDateToString(date, "yyyy-MM-dd"));
        // TODO : dans le web service faire en sorte qu'on emprunte le premier exemplaire disponible à cette date
        return document;
    }

}