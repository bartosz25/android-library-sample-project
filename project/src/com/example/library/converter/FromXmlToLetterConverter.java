package com.example.library.converter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Node;

import android.util.Log;

import com.example.library.model.entity.Letter; 

public class FromXmlToLetterConverter {

    private static final String LOG_TAG = FromXmlToLetterConverter.class.getName();

    public static Letter convert(Node node, Map<String, String> configuration) {
        Log.d(LOG_TAG, "Converting " + node);

        String code = "";
        Node codeNode = node.selectSingleNode("./code");
        if (codeNode != null) code = codeNode.getText();
        String itemsQuantity = "";
        Node quantityNode = node.selectSingleNode("./quantity");
        if (quantityNode != null) itemsQuantity = quantityNode.getText();
        String iterator = "";
        Node iteratorNode = node.selectSingleNode("./iterator");
        if (iteratorNode != null) iterator = iteratorNode.getText();

        Letter letter = new Letter(); 
        letter.setCode(code);
        if (!itemsQuantity.equals("")) {
            letter.setElementsCounter(UniversalConverter.fromStringToInt(itemsQuantity));
        }
        if (!iterator.equals("")) {
            letter.setId(UniversalConverter.fromStringToInt(iterator));
        }
        Log.d(LOG_TAG, "Generating letter " + letter);
        return letter;
    }

}