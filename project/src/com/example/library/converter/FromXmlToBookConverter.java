package com.example.library.converter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Node;

import android.util.Log;

import com.example.library.model.entity.Book;
import com.example.library.model.entity.BookCopy;
import com.example.library.model.entity.Category;
import com.example.library.model.entity.Writer;

public class FromXmlToBookConverter {

    private static final String LOG_TAG = FromXmlToBookConverter.class.getName();

    public static Book convert(Node node, Map<String, String> configuration) {
    Log.d(LOG_TAG, "Converting " + node);
    List<Writer> writersList = new ArrayList<Writer>();
    List<Node> writers = node.selectNodes("./writers/writer");

    for (Iterator<Node> iterator = writers.iterator(); iterator.hasNext();) {
        Node writerNode = (Node) iterator.next();
        writersList.add(FromXmlToWriterConverter.convert(writerNode, configuration));
    }

    List<Category> categoriesList = new ArrayList<Category>();
    List<Node> categories = node.selectNodes("./categories/category");

    for (Iterator<Node> iterator = categories.iterator(); iterator.hasNext();) {
        Node categoryNode = (Node) iterator.next();
        categoriesList.add(FromXmlToCategoryConverter.convert(categoryNode));
    }

    List<BookCopy> bookCopiesList = new ArrayList<BookCopy>();
    List<Node> bookCopies = node.selectNodes("./book-copies/book-copy");

    for (Iterator<Node> iterator = bookCopies.iterator(); iterator.hasNext();) {
        Node bookCopyNode = (Node) iterator.next();
        bookCopiesList.add(FromXmlToBookCopyConverter.convert(bookCopyNode));
    }

    long id = 0l;
    Node idNode= node.selectSingleNode("./book/id");
    if (idNode != null) {
        id = UniversalConverter.fromStringToLong(idNode.getText());
    }
    String title = "";
    Node titleNode = node.selectSingleNode("./book/title");
    if (titleNode != null) title = titleNode.getText();
    String lead = "";
    Node leadNode = node.selectSingleNode("./book/lead");
    if (leadNode != null) lead = leadNode.getText();
    String description = "";
    Node descriptionNode = node.selectSingleNode("./book/description");
    if (descriptionNode != null) description = descriptionNode.getText();
    String review = "";
    Node reviewNode = node.selectSingleNode("./book/review");
    if (reviewNode != null) review = reviewNode.getText();
    String url = "";
    Node urlNode = node.selectSingleNode("./book/url");
    if (urlNode != null) url = urlNode.getText();

    Book book = new Book(); 
    book.setId(id);
    book.setTitle(title);
    book.setUrl(url);
    book.setLead(lead);
    book.setDescription(description);
    book.setReview(review);
    book.setWriters(writersList);
    book.setCategories(categoriesList);
    book.setBookCopies(bookCopiesList);
    Log.d(LOG_TAG, "Generating book" + book);
    return book;
    }

}