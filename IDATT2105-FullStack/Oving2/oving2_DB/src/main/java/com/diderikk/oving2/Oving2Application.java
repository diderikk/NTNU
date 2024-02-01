package com.diderikk.oving2;

import java.util.List;

import com.diderikk.oving2.model.Address;
import com.diderikk.oving2.model.Author;
import com.diderikk.oving2.model.Book;
import com.diderikk.oving2.repository.AddressRepository;
import com.diderikk.oving2.repository.AuthorRepository;
import com.diderikk.oving2.repository.BookRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Oving2Application {

	public static void main(String[] args) {
		SpringApplication.run(Oving2Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(BookRepository bookRepository, AuthorRepository authorRepository, AddressRepository addressRepository){
		return args -> {
			Book book1 = new Book("Book",24);
			Book book2 = new Book("Test2",420);

			Author author1 = new Author("Diderik Kramer","diderik.kramer@gmail.com");
			Author author2 = new Author("Didkra" , "diderikk@stud.ntnu.no");

			Address address1 = new Address("Grimseidvegen", 435);

			addressRepository.save(address1);
			bookRepository.saveAll(List.of(book1,book2));
			authorRepository.saveAll(List.of(author1, author2));
		};
	}
}
