package com.diderikk.oving2.service;

import java.util.List;
import java.util.Optional;

import com.diderikk.oving2.model.Author;
import com.diderikk.oving2.model.Book;
import com.diderikk.oving2.repository.AuthorRepository;
import com.diderikk.oving2.repository.BookRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AuthorRepository authorRepository;
    
    private final static Logger logger = LoggerFactory.getLogger(BookService.class);

    public List<Book> getBooks(){
        logger.warn("getBooks() called");
        return bookRepository.findAll();
    }

    public Book getBookByTitle(String title){
        logger.warn("getBooksByTitle(String title) called: " + title);
        return bookRepository.findBookByTitle(title).get();
    }

    public List<Book> getBooksByAuthor(long id){
        logger.warn("getBooksByAuthor(long id) called: " + id);
        return authorRepository.findById(id).get().getBooks();
    }

    public List<Author> getAuthors(long id){
        logger.warn("getAuthors(long id) called: " + id);
        Optional<Book> book = bookRepository.findById(id);
        return book.get().getAuthors();
    }

    public Author getAuthor(long id, String authorName){
        logger.warn("getAuthor(long id, String authorName) called: " + id + " " + authorName);
        Optional<Book> book = bookRepository.findById(id);
        if(book.isPresent()){
            Book temp = book.get();
            for(Author author : temp.getAuthors()){
                if(author.getName().equals(authorName)) return author;
            }
        }
        return null;
    }

    public Book addBook(Book book){
        return bookRepository.save(book);
    }

    public Book putBookByID(long id, Book book){
        Optional<Book> temp = bookRepository.findById(id);
        if(temp.isPresent()){
            temp.get().setTitle(book.getTitle());
            temp.get().setPageAmount(book.getPageAmount());
            temp.get().setAuthors(book.getAuthors());
        }
        return bookRepository.save(temp.get());
    }

    public Book deleteBookByID(long id){
        Optional<Book> book = bookRepository.findById(id);
        if(book.isPresent()){
            System.out.println(book.get());
            bookRepository.deleteById(id);
            return book.get();
        }
        else{
            return null;
        }
    }
}
