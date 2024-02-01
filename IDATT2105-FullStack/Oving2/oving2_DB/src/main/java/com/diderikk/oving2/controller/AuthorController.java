package com.diderikk.oving2.controller;

import java.util.List;

import com.diderikk.oving2.model.Address;
import com.diderikk.oving2.model.Author;
import com.diderikk.oving2.model.Book;
import com.diderikk.oving2.service.AuthorService;

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
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/authors")
    public List<Author> getAuthors(){
        return authorService.getAuthors();
    }

    @GetMapping("/authors/{id}")
    public Author getAuthor(@PathVariable("id") Long id){
        return authorService.getAuthor(id);
    }

    //localhost:8080/authors?name=Diderik%20Kramer
    @GetMapping("/author")
    public Author getAuthorByName(@RequestParam String name){
        return authorService.getAuthorByName(name);
    }

    @PostMapping("/authors")
    public Author addAuthor(@RequestBody Author author){
        return authorService.addAuthor(author);
    }

    @PutMapping("/authors/{id}")
    public Author updateAuthor(@PathVariable("id") Long id,@RequestBody Author author){
        return authorService.updateAuthor(id, author);
    }

    @PutMapping("/authors/{id}/books")
    public Author addBook(@PathVariable("id") Long id, @RequestBody Book book){
        return authorService.addBook(id, book);
    }

    @PutMapping("/authors/{id}/books/{bookID}")
    public Author addExistingBook(@PathVariable("id") Long id, @PathVariable("bookID") Long bookID){
        return authorService.addExistingBook(id, bookID);
    }

    @PutMapping("/authors/{id}/address")
    public Author addNewAddress(@PathVariable("id") Long id, @RequestBody Address address){
        return authorService.addNewAddress(id, address);
    }

    @PutMapping("/authors/{id}/address/{addressID}")
    public Author addExistingAddress(@PathVariable("id") Long id, @PathVariable("addressID") Long addressID){
        return authorService.addExistingAddress(id, addressID);
    }

    @DeleteMapping("/authors/{id}/books/{bookID}")
    public Author removeBook(@PathVariable("id") Long id, @PathVariable("bookID") Long bookID){
        return authorService.removeBook(id, bookID);
    }

    @DeleteMapping("/authors/{id}/address")
    public Author removeAddress(@PathVariable("id") Long id){
        return authorService.removeAddress(id);
    }

    @DeleteMapping("/authors/{id}")
    public Author removeAuthor(@PathVariable("id") Long id){
        return authorService.deleteAuthor(id);
    }
}
