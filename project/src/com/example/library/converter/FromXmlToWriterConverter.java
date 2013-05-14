package com.example.library.converter;

import java.util.Date;
import java.util.Map;

import org.dom4j.Node;

import android.util.Log;

import com.example.library.model.entity.Writer;

public class FromXmlToWriterConverter {

    private static final String LOG_TAG = FromXmlToWriterConverter.class.getName();

    public static Writer convert(Node node, Map<String, String> configuration) {
        Log.d(LOG_TAG, "Converting " + node);

        Writer writer = new Writer();

        long id = 0l;
        Node idNode = node.selectSingleNode("./id");
        if (idNode != null) {
            id = UniversalConverter.fromStringToLong(idNode.getText());
        }
        String firstName = "";
        Node firstNameNode = node.selectSingleNode("./first-name");
        if (firstNameNode != null) firstName = firstNameNode.getText();    
        String famillyName ="";
        Node famillyNameNode = node.selectSingleNode("./familly-name");
        if (famillyNameNode != null) famillyName = famillyNameNode.getText();

        Date bornDate = null;
        Node bornNode = node.selectSingleNode("./born-date");
        if (bornNode != null && !bornNode.getText().trim().equals("")) {
            writer.setBornDate(UniversalConverter.fromStringToDate(bornNode.getText(), configuration.get("date-format")));
        }

        Node deadNode = node.selectSingleNode("./dead-date");
        if (deadNode != null && !deadNode.getText().trim().equals("")) {
            writer.setDeadDate(UniversalConverter.fromStringToDate(deadNode.getText(), configuration.get("date-format")));
        }

        writer.setId(id);
        writer.setFirstName(firstName);
        writer.setFamillyName(famillyName);
        return writer;
    }

}