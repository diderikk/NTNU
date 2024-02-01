package com.diderikk.oving2.repository;

import java.util.Optional;

import com.diderikk.oving2.model.Book;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    // @Query("SELECT b FROM Book b WHERE s.title = ?1")
    Optional<Book> findBookByTitle(String title);

}
