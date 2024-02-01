package com.diderikk.ex2.service;

import java.util.List;

import com.diderikk.ex2.model.Author;
import com.diderikk.ex2.model.Book;
import com.diderikk.ex2.repository.AuthorDAO;
import com.diderikk.ex2.repository.BookDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @Autowired
    private BookDAO bookDAO;
    @Autowired
    private AuthorDAO authorDAO;

    public List<Book> getBooks(){
        return bookDAO.getBooks();
    }

    public Book getBookByID(long id){
        return bookDAO.getBookByID(id);
    }

    public Book addBook(Book book){
        return bookDAO.addBook(book);
    }

    public Book updateBook(long id, Book book){
        return bookDAO.updateBook(id, book);
    }

    public Book addNewAuthor(long id, Author author){
        Book book = getBookByID(id);
        book.getAuthors().add(author);
        return updateBook(id, book);
    }

    public Book addExistingAuthor(long id, long authorID){
        Book book = getBookByID(id);
        Author author = authorDAO.getByID(id);
        book.getAuthors().add(author);
        return updateBook(id, book);
    }

    public Book removeBook(long id){
        return bookDAO.removeBookByID(id);
    }
}
