package com.diderikk.ex2;

import com.diderikk.ex2.model.Address;
import com.diderikk.ex2.model.Author;
import com.diderikk.ex2.model.Book;
import com.diderikk.ex2.repository.AddressDAO;
import com.diderikk.ex2.repository.AuthorDAO;
import com.diderikk.ex2.repository.BookDAO;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Ex2Application {

	public static void main(String[] args) {
		SpringApplication.run(Ex2Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(AuthorDAO authorDAO, BookDAO bookDAO, AddressDAO addressDAO){
		return args -> {
			Book book1 = new Book("Book",24);
			Book book2 = new Book("Test2",420);

			Author author1 = new Author("Diderik Kramer","diderik.kramer@gmail.com");
			Author author2 = new Author("Didkra" , "diderikk@stud.ntnu.no");

			Address address1 = new Address("Grimseidvegen", 435);

			addressDAO.addAddress(address1);
			bookDAO.addBook(book1);
			bookDAO.addBook(book2);
			authorDAO.addAuthor(author1);
			authorDAO.addAuthor(author2);
		};
	}

}
