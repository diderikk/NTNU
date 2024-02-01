package com.diderikk.ex2.controller;

import java.util.List;

import com.diderikk.ex2.model.Book;
import com.diderikk.ex2.service.BookService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/books")
    public List<Book> getBooks(){
        return bookService.getBooks();
    }

    @GetMapping("/book/{id}")
    public Book getBook(@PathVariable("id") Long id){
        return bookService.getBookByID(id);
    }

    @PostMapping("/books")
    public Book registerBook(@RequestBody Book book){
        return bookService.addBook(book);
    }

    @PutMapping("/book/{id}")
    public Book updateBook(@PathVariable("id") Long id, @RequestBody Book book){
        return bookService.updateBook(id, book);
    }

    @DeleteMapping("/book/{id}")
    public Book deleteBook(@PathVariable("id") Long id){
        return bookService.removeBook(id);
    }
}
