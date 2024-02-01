package com.diderikk.ex2.repository;

import java.util.ArrayList;
import java.util.List;

import com.diderikk.ex2.model.Book;

import org.springframework.stereotype.Repository;

@Repository
public class BookDAO {
    private List<Book> books;
    private int length;

    public BookDAO(){
        this.books = new ArrayList<>();
        this.length = 0;
    }

    public BookDAO(List<Book> books){
        this.books = books;
        this.length = 0;
    }

    public List<Book> getBooks(){
        return books;
    }

    public Book getBookByID(long id){
        for(Book book : books){
            if(book.getBookID() == id) return book;
        }
        return null;
    }

    public Book addBook(Book book){
        book.setBookID(++length);
        books.add(book);
        return book;
    }

    public Book updateBook(long id, Book book){
        int index = -1;
        for(int i = 0; i<books.size(); i++){
            if(books.get(i).getBookID() == id){
                book.setBookID(books.get(i).getBookID());
                index = i;
            }
        }
        books.set(index, book);
        return book;
    }

    public Book removeBookByID(long id){
        for(Book book : books){
            if(book.getBookID() == id){
                books.remove(book);
                return book;
            }
        }
        return null;
    }
}
