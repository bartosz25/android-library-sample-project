package com.example.library.converter;

import org.dom4j.Node;

import android.util.Log;

import com.example.library.model.entity.BookCopy;

public class FromXmlToBookCopyConverter {

    private static final String LOG_TAG = FromXmlToBookCopyConverter.class.getName();

    public static BookCopy convert(Node node) {
        Log.d(LOG_TAG, "Converting " + node);
        String reference = "";
        Node referenceNode = node.selectSingleNode("./reference");
        if (referenceNode != null) reference = referenceNode.getText();

        String condition = "";
        Node conditionNode = node.selectSingleNode("./condition");
        if (conditionNode != null) condition = conditionNode.getText();

        String state = "";
        Node stateNode = node.selectSingleNode("./state");
        if (stateNode != null) state = stateNode.getText();

        BookCopy bookCopy = new BookCopy();
        bookCopy.setReference(reference);
        bookCopy.setCondition(condition);
        bookCopy.setState(state);
        return bookCopy;
    }

}