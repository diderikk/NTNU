package com.diderikk.ex2.model;

import java.util.ArrayList;
import java.util.List;


public class Author{
    private long authorID;
    private String name;
    private String email;

    List<Book> books;

    private Address address;

    public Author(){
        this.books = new ArrayList<>();
    }

    public Author(String name, String email){
        this.name = name;
        this.email = email;
        this.books = new ArrayList<>();
    }

    public long getAuthorID(){
        return authorID;
    }

    public void setAuthorID(long id){
        this.authorID = id;
    }

    public String getName(){
        return name;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public List<Book> getBooks(){
        return books;
    }

    public void setBooks(List<Book> books){
        this.books = books;
    }

    public Address getAddress(){
        return address;
    }

    public void setAddress(Address address){
        this.address = address;
    }

    public String toString(){
        return "Author: " + authorID + "\nName: " + name + "\nEmail: " + email;
    }

    
}
