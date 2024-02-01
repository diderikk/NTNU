package com.diderikk.ex2.repository;

import java.util.ArrayList;
import java.util.List;

import com.diderikk.ex2.model.Author;

import org.springframework.stereotype.Repository;

@Repository
public class AuthorDAO {
    private List<Author> authors;
    private int length;

    public AuthorDAO(){
        authors = new ArrayList<>();
        this.length = 0;
    }

    public AuthorDAO(List<Author> authors){
        this.authors = authors;
        this.length = 0;
    }

    public List<Author> getAuthors(){
        return authors;
    }

    public Author getByID(long id){
        for(Author author : authors){
            if(author.getAuthorID() == id) return author;
        }
        return null;
    }

    public Author addAuthor(Author author){
        author.setAuthorID(++length);
        authors.add(author);
        return author;
    }

    public Author updateAuthor(long id, Author author){
        int index = -1;
        for(int i = 0; i<authors.size(); i++){
            if(authors.get(i).getAuthorID() == id){
                author.setAuthorID(authors.get(i).getAuthorID());
                index = i;
            }
        }
        authors.set(index, author);
        return author;
    }

    public Author removeAuthorByID(long id){
        for(Author author : authors){
            if(author.getAuthorID() == id){
                authors.remove(author);
                return author;
            }
        }
        return null;
    }

}
