package com.diderikk.oving2.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "author")
public class Author{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long authorID;
    private String name;
    private String email;

    @ManyToMany
    @JoinTable(
        name="author_book",
        joinColumns = @JoinColumn(name = "authorID"),
        inverseJoinColumns = @JoinColumn(name = "bookID"))
    List<Book> books;

    @OneToOne
    @JoinColumn(name = "addressID", referencedColumnName = "addressID")
    private Address address;

    public Author(){}

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

    public void setName(String name){
        this.name = name;
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