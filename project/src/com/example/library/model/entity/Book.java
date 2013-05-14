package com.example.library.model.entity;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class Book implements Serializable {

    private long id;
    private String title;
    private String url;
    private String lead;
    private String description;
    private String review;
    private List<Writer> writers;
    private List<Category> categories;
    private List<BookCopy> bookCopies;

    public Book() {
        
    }

    public Book(long id, String title, String url, String description, String lead, String review, List<Writer> writers) {
        setId(id);
        setTitle(title);
        setDescription(description);
        setLead(lead);
        setReview(review);
        setWriters(writers);
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setLead(String lead) {
        this.lead = lead;
    }
    public void setReview(String review) {
        this.review = review;
    }
    public void setWriters(List<Writer> writers) {
        this.writers = writers;
    }
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
    public void setBookCopies(List<BookCopy> bookCopies) {
        this.bookCopies = bookCopies;
    }

    public long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getUrl() {
        return url;
    }
    public String getDescription() {
        return description;
    }
    public String getLead() {
        return lead;
    }
    public String getReview() {
        return review;
    }
    public List<Writer> getWriters() {
        return writers;
    }
    public List<Category> getCategories() {
        return categories;
    }
    public List<BookCopy> getBookCopies() {
        return bookCopies;
    }
    public String getWritersString() {
        StringBuffer stringBuffer = new StringBuffer();
        Iterator<Writer> writerIterator = writers.iterator();
        while(writerIterator.hasNext()) {
            Writer writer = (Writer) writerIterator.next();
            stringBuffer.append(writer.getFirstName() + " " + writer.getFamillyName());
            if(writerIterator.hasNext()) stringBuffer.append(",");
        }
        return stringBuffer.toString();
    }

    public String toString() {
        return "Book [id = "+getId()+", title = "+getTitle()+", url = "+getUrl()+", lead = "+getLead()+", review = "+getReview()+", description = "+getDescription()+", writers = "+getWriters()+
        "categories = "+getCategories()+", bookCopies = "+getBookCopies()+"]";
    }

}