package com.diderikk.oving2.service;

import java.util.List;
import java.util.Optional;

import com.diderikk.oving2.model.Address;
import com.diderikk.oving2.model.Author;
import com.diderikk.oving2.model.Book;
import com.diderikk.oving2.repository.AddressRepository;
import com.diderikk.oving2.repository.AuthorRepository;
import com.diderikk.oving2.repository.BookRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private AddressRepository addressRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorService.class);

    public List<Author> getAuthors(){
        LOGGER.warn("getAuthors() called: ");
        return authorRepository.findAll();
    }

    public Author getAuthor(long id){
        LOGGER.warn("getAuthor(long id) called: " + id);
        return authorRepository.findById(id).get();
    }

    public Author getAuthorByName(String name){
        LOGGER.warn("getAuthorByName(String name) called: " + name);
        System.out.println("Hello");
        return authorRepository.findAuthorByName(name);
    }

    public Author addAuthor(Author author){
        return authorRepository.save(author);
    }

    public Author updateAuthor(long id, Author author){
        Author temp = getAuthor(id);
        temp.setAddress(author.getAddress());
        temp.setBooks(author.getBooks());
        temp.setEmail(author.getEmail());
        temp.setName(author.getName());
        return authorRepository.save(temp); 
    }

    public Author addNewAddress(long id, Address address){
        Optional<Author> author = authorRepository.findById(id);
        if(author.isPresent()){
            addressRepository.save(address);
            author.get().setAddress(address);
            return authorRepository.save(author.get());
        }
        return null;
    }

    public Author addExistingAddress(long id, long addressID){
        Optional<Author> author = authorRepository.findById(id);
        Optional<Address> address = addressRepository.findById(addressID);
        if(author.isPresent() && address.isPresent()){
            author.get().setAddress(address.get());
            return authorRepository.save(author.get());
        }
        return null;
    }

    public Author removeAddress(long id){
        Optional<Author> author = authorRepository.findById(id);
        if(author.isPresent()){
            author.get().setAddress(null);
            return authorRepository.save(author.get());
        }
        return null;
    }

    public Author addBook(long id, Book book){
        Author author = authorRepository.findById(id).get();
        bookRepository.save(book);
        author.getBooks().add(book);
        return authorRepository.save(author);
    }

    public Author addExistingBook(long id, long bookID){
        Optional<Author> author = authorRepository.findById(id);
        Optional<Book> book = bookRepository.findById(bookID);
        if(author.isPresent() && book.isPresent()){
            System.out.println(book.get());
            System.out.println(author.get());
            author.get().getBooks().add(book.get());
            return authorRepository.save(author.get());
        }
        return null;
    }

    public Author deleteAuthor(long id){
        Author author = authorRepository.findById(id).get();
        authorRepository.deleteById(id);
        return author;
    }

    public Author removeBook(long id, long bookID){
        Optional<Author> author = authorRepository.findById(id);
        if(author.isPresent()){
            removeBookByID(author.get(), bookID);
            return authorRepository.save(author.get());
        }
        return author.get();
        
    }

    private void removeBookByID(Author author ,long bookID){
        List<Book> list = author.getBooks();
        for(Book temp : list){
            if(temp.getBookID() == bookID){
                list.remove(temp);
                break;
            }
        }
    }
    
}
