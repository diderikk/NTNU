package com.diderikk.ex2.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Book {
    private long bookID;
    private String title;
    private int pageAmount;

    @JsonIgnore
    List<Author> authors;

    public Book(){
        this.authors = new ArrayList<>();
    }

    public Book(String title, int pageAmount){
        this.title = title;
        this.pageAmount = pageAmount;
        this.authors = new ArrayList<>();
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public int getPageAmount(){
        return pageAmount;
    }

    public void setPageAmount(int pageAmount){
        this.pageAmount = pageAmount;
    }

    public long getBookID(){
        return bookID;
    }

    public void setBookID(long id){
        this.bookID = id;
    }

    public List<Author> getAuthors(){
        return authors;
    }

    public void setAuthors(List<Author> authors){
        this.authors = authors;
    }
    
    public String toString(){
        return "Book: " + bookID + "\nTitle: " + title + "\nPage Amount: " + pageAmount;
    }
}

