package com.example.library.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Node;

import android.util.Log;

import com.example.library.model.entity.Category;

public class FromXmlToCategoryConverter {

    private static final String LOG_TAG = FromXmlToCategoryConverter.class.getName();

    public static Category convert(Node node) {
        Log.d(LOG_TAG, "Converting " + node);

        long id = 0l;
        Node idNode = node.selectSingleNode("./id");
        if (idNode != null) {
            id = UniversalConverter.fromStringToLong(idNode.getText());
        }
        String name = "";
        Node nameNode = node.selectSingleNode("./name");
        if (nameNode != null) name = nameNode.getText();
        Map<String, String> urls = new HashMap<String, String>();
        List<Node> urlNodes = node.selectNodes("./urls/item");

        for (Iterator<Node> iterator = urlNodes.iterator(); iterator.hasNext();) {
            Node urlNode = (Node) iterator.next();
            if (urlNode != null) {
                Attribute nameAtt = (Attribute) node.selectObject("./urls/item/@name");
                Log.d(LOG_TAG, "Found nameAtt" + nameAtt);
                urls.put(nameAtt.getText(), urlNode.getText());
            }
        }

        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setUrls(urls);
        return category;
    }

}
