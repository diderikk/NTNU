package com.diderikk.oving2.controller;

import java.util.List;

import com.diderikk.oving2.model.Author;
import com.diderikk.oving2.model.Book;
import com.diderikk.oving2.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public List<Book> getBooks(){
        return bookService.getBooks();
    }

    @GetMapping("/books/{id}/authors")
    public List<Author> getAuthors(@PathVariable("id") Long id){
        return bookService.getAuthors(id);
    }

    //localhost:8080/books?title=Test2
    @GetMapping("/book")
    public Book getBookByTitle(@RequestParam String title){
        return bookService.getBookByTitle(title);
    }

    @GetMapping("/books/{authorID}")
    public List<Book> getBooksByAuthor(@PathVariable("authorID") Long id){
        return bookService.getBooksByAuthor(id);
    }

    @PostMapping("/books")
    public Book registerBook(@RequestBody Book book){
        return bookService.addBook(book);
    }

    @PutMapping("/books/{id}")
    public Book putBook(@PathVariable("id") Long id, @RequestBody Book book){
        return bookService.putBookByID(id, book);
    }


    @DeleteMapping("/books/{id}")
    public Book deleteBook(@PathVariable("id") Long id){
        return bookService.deleteBookByID(id);
    }

}
