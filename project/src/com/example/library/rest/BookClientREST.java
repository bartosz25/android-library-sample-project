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
import com.example.library.logger.Logger;
import com.example.library.model.entity.Book;

public class BookClientREST extends ClientREST {

    public static final String URL_BOOKS_LIST = "/web-service/books.xml"; //"/web-service/books/get-list";
    public static final String URL_BOOK_BY_ID = "/web-service/book.xml"; //"/web-service/books/get-per-id/{ID}";
    protected final static String LOG_TAG = BookClientREST.class.getName();

    public Map<String, Object> getBook(long id) {
        return parseResponse(super.call(URL_BOOK_BY_ID, METHOD_GET, constructOneBookXml(id)));
    }

    public Map<String, Object> getBooks(Map<String, Object> params) {
        return parseResponseList(super.call(URL_BOOKS_LIST, METHOD_GET, constructBooksListXml(params)));
    }

    public Map<String, Object> parseResponse(String response) {
        if(response == null || response.trim().equals("")) return null;
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            Document document = DocumentHelper.parseText(response);
            Map<String, String> config = getConfiguration(document.selectSingleNode(XPATH_CONFIG));
            Map<String, String> state = getResponseState(document.selectSingleNode(XPATH_STATE));
            Book book = FromXmlToBookConverter.convert(document.selectSingleNode("/response/content"), config);
            result.put(KEY_CONFIG, config);
            result.put(KEY_STATE, state);
            result.put("book", book);
            return result;
        } catch (DocumentException e) {
            Logger.addMessage(LOG_TAG, "An error occured on parsing response for book request : " + e.getMessage());
            Log.e(LOG_TAG, "An error occured on generating Document from " + response, e);
        }
        return null;
    }

    public Map<String, Object> parseResponseList(String response) {
        if(response == null || response.trim().equals("")) return null;
        try {
            Map<String, Object> result = new HashMap<String, Object>();
            Document document = DocumentHelper.parseText(response);
            Map<String, String> config = getConfiguration(document.selectSingleNode(XPATH_CONFIG));
            Map<String, String> state = getResponseState(document.selectSingleNode(XPATH_STATE));

            List<Book> booksList = new ArrayList<Book>();
            List<Node> books = document.selectNodes("/response/books/item");
            for(Iterator<Node> iterator = books.iterator(); iterator.hasNext();) {
                Node node = (Node) iterator.next();
                booksList.add(FromXmlToBookConverter.convert(node.selectSingleNode("."), config));		
            }
            Log.d(LOG_TAG, "Found books list " + booksList);
            result.put(KEY_CONFIG, config);
            result.put(KEY_STATE, state);
            result.put("books", booksList);
            return result;
        } catch (DocumentException e) {
            Logger.addMessage(LOG_TAG, "An error occured on parsing response of books list : " + e.getMessage());
            Log.e(LOG_TAG, "An error occured on generating Document from " + response, e);
        }				
        return null;
    }

    private Document constructOneBookXml(long id) {
        Document document =  DocumentHelper.createDocument();
        Element root = document.addElement("request");
        constructConfig(root, null);
        Element criteria = root.addElement("criteria");
        Element criterion = criteria.addElement("criterion");
        criterion.addElement("field").addText("id");
        criterion.addElement("value").addText(""+id);
        return document;
    }

    private Document constructBooksListXml(Map<String, Object> params) {
        Document document =  DocumentHelper.createDocument();
        Element root = document.addElement("request");
        constructConfig(root, null);
        // offset
        Element offset = root.addElement("offset");
        offset.addElement("from").addText(""+params.get("from"));
        offset.addElement("limit").addText(""+params.get("limit"));
        // criteria
        Element criteria = root.addElement("criteria");
        Map<String, List<String>> criteriaMap = (Map<String, List<String>>) params.get("criteria");
        for (Map.Entry<String, List<String>> mapEntry : criteriaMap.entrySet()) {
            Element criterion = criteria.addElement("criterion");
            criterion.addElement("field").addText(mapEntry.getKey());
            for (String key : mapEntry.getValue()) {
                criterion.addElement("value").addText(key);
            }
        }
        return document;
    }

}