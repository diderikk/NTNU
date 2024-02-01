package com.diderikk.ex2.service;

import java.util.List;

import com.diderikk.ex2.model.Address;
import com.diderikk.ex2.model.Author;
import com.diderikk.ex2.model.Book;
import com.diderikk.ex2.repository.AuthorDAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    @Autowired
    private AuthorDAO authorDAO;
    @Autowired
    private BookService bookService;
    @Autowired
    private AddressService addressService;

    public List<Author> getAuthors(){
        return authorDAO.getAuthors();
    }

    public Author getAuthor(long id){
        return authorDAO.getByID(id);
    }

    public Author addAuthor(Author author){
        return authorDAO.addAuthor(author);
    }

    public Author updateAuthor(long id, Author author){
        return authorDAO.updateAuthor(id, author);
    }

    public Author addNewBook(long id, Book book){
        Author author = getAuthor(id);
        bookService.addBook(book);
        author.getBooks().add(book);
        bookService.addNewAuthor(book.getBookID(), author);
        return updateAuthor(id, author);
    }

    public Author addExistingBook(long id, long bookID){
        Author author = getAuthor(id);
        Book book = bookService.getBookByID(bookID);
        bookService.addNewAuthor(book.getBookID(), author);
        author.getBooks().add(book);
        return updateAuthor(id, author);
    }

    public Author addNewAddress(long id, Address address){
        Author author = getAuthor(id);
        author.setAddress(address);
        addressService.addNewAuthor(address.getAddressID(), author);
        return updateAuthor(id, author);
    }

    public Author removeAuthor(long id){
        return authorDAO.removeAuthorByID(id);
    }
}
