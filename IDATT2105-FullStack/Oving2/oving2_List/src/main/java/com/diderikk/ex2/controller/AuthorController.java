package com.diderikk.ex2.controller;

import java.util.List;

import com.diderikk.ex2.model.Address;
import com.diderikk.ex2.model.Author;
import com.diderikk.ex2.model.Book;
import com.diderikk.ex2.service.AuthorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthorController {
    @Autowired
    private AuthorService authorService;

    @GetMapping("/authors")
    public List<Author> getAuthors(){
        return authorService.getAuthors();
    }

    @GetMapping("/author/{id}")
    public Author getAuthor(@PathVariable("id") Long id){
        return authorService.getAuthor(id);
    }

    @PostMapping("/authors")
    public Author addAuthor(@RequestBody Author author){
        return authorService.addAuthor(author);
    }

    @PutMapping("/author/{id}")
    public Author updateAuthor(@PathVariable("id") Long id, @RequestBody Author author){
        return authorService.updateAuthor(id, author);
    }

    @PutMapping("/author/{id}/books")
    public Author addNewBook(@PathVariable("id") Long id, @RequestBody Book book){
        return authorService.addNewBook(id, book);
    }

    @PutMapping("/author/{id}/book/{bookID}")
    public Author addExistingBook(@PathVariable("id") Long id, @PathVariable("bookID") Long bookID){
        return authorService.addExistingBook(id, bookID);
    }

    @PutMapping("/author/{id}/address")
    public Author addNewAddress(@PathVariable("id") Long id, @RequestBody Address address){
        return authorService.addNewAddress(id, address);
    }

    @DeleteMapping("/author/{id}")
    public Author removeAuthor(@PathVariable("id") Long id){
        return authorService.removeAuthor(id);
    }
}
