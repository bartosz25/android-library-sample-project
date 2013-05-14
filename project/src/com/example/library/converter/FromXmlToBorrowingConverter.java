package com.example.library.converter;

import java.util.Map;

import org.dom4j.Node;

import android.util.Log;

import com.example.library.model.entity.Borrowing;

public class FromXmlToBorrowingConverter {

    private static final String LOG_TAG = FromXmlToBorrowingConverter.class.getName();

    public static Borrowing convert(Node node, Map<String, String> configuration) {
        Log.d(LOG_TAG, "Converting " + node);

        long id = 0l;
        Node idNode = node.selectSingleNode("./id");
        if (idNode != null) {
            id = UniversalConverter.fromStringToLong(idNode.getText());
        }

        int alerts = 0;
        Node alertsNode = node.selectSingleNode("./alerts");
        if (alertsNode != null) {
            alerts = UniversalConverter.fromStringToInt(alertsNode.getText());
        }

        int penaltyAttached = 0;
        Node penaltyAttachedNode = node.selectSingleNode("./penalty-attached");
        if (penaltyAttachedNode != null) {
            penaltyAttached = UniversalConverter.fromStringToInt(penaltyAttachedNode.getText());
        }

        Borrowing borrowing = new Borrowing();
        borrowing.setId(id);
        borrowing.setAlerts(alerts);
        borrowing.setPenaltyAttached(penaltyAttached);
        borrowing.setBookCopy(FromXmlToBookCopyConverter.convert(node.selectSingleNode("./book-copy")));

        Node borrowingDateNode = node.selectSingleNode("./borrowing-date");
        if (borrowingDateNode != null && !borrowingDateNode.getText().trim().equals("")) {
            borrowing.setBorrowingDate(UniversalConverter.fromStringToDate(borrowingDateNode.getText(), configuration.get("date-format")));
        }

        Node returnDateNode = node.selectSingleNode("./return-date");
        if (returnDateNode != null && !returnDateNode.getText().trim().equals("")) {
            borrowing.setReturnDate(UniversalConverter.fromStringToDate(returnDateNode.getText(), configuration.get("date-format")));
        }

        return borrowing;
    }

}