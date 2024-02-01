package com.diderikk.oving2;

import com.diderikk.oving2.model.Address;
import com.diderikk.oving2.model.Author;
import com.diderikk.oving2.model.Book;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void shouldReturnFirstAuthor() throws Exception {
        this.mockMvc.perform(get("/authors/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.authorID", is(1)))
        .andExpect(jsonPath("$.name", containsStringIgnoringCase("Diderik")))
        .andExpect(jsonPath("$.email", containsStringIgnoringCase("diderik.kramer@gmail.com")))
        .andReturn();
    }

    @Test
    public void shouldReturnFirstAuthorByName() throws Exception {
        this.mockMvc.perform(get("/author").param("name", "Diderik Kramer"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.authorID", is(1)))
        .andReturn();
    }

    @Test
    public void shouldPostAndReturnANewAuthor() throws Exception {
        Author author = new Author("Test","test@test.no");
        String authorJson = objectMapper.writeValueAsString(author);

        this.mockMvc.perform(post("/authors")
        .contentType(MediaType.APPLICATION_JSON).content(authorJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", containsStringIgnoringCase("Test")))
        .andExpect(jsonPath("$.email", containsStringIgnoringCase("test@test.no")));
    }

    @Test
    public void shouldUpdateAuthor() throws Exception {
        Author author = new Author("Test1","test1@test.no");
        String authorJson = objectMapper.writeValueAsString(author);

        this.mockMvc.perform(put("/authors/2")
        .contentType(MediaType.APPLICATION_JSON)
        .content(authorJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", containsStringIgnoringCase("Test1")))
        .andExpect(jsonPath("$.email", containsStringIgnoringCase("test1@test.no")))
        .andReturn();

        this.mockMvc.perform(get("/authors/2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", containsStringIgnoringCase("Test1")))
        .andExpect(jsonPath("$.email", containsStringIgnoringCase("test1@test.no")))
        .andReturn();
    }
    
    @Test
    public void shouldAddNewBookToAuthor() throws Exception {
        Book book = new Book("Test", 420);
        String bookJson = objectMapper.writeValueAsString(book);

        this.mockMvc.perform(put("/authors/1/books")
        .contentType(MediaType.APPLICATION_JSON)
        .content(bookJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.books[0].title", containsStringIgnoringCase("Test")))
        .andExpect(jsonPath("$.books[0].pageAmount", is(420)));

        this.mockMvc.perform(get("/authors/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.books[0].title", containsStringIgnoringCase("Test")))
        .andExpect(jsonPath("$.books[0].pageAmount", is(420)));

    }

    // Book book1 = new Book("Book",24);
    @Test
    public void shouldAddExistingBookToAuthor() throws Exception {
        this.mockMvc.perform(put("/authors/2/books/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.books[0].title", containsStringIgnoringCase("Book")))
        .andExpect(jsonPath("$.books[0].pageAmount", is(24)));

        this.mockMvc.perform(get("/authors/2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.books[0].title", containsStringIgnoringCase("Book")))
        .andExpect(jsonPath("$.books[0].pageAmount", is(24)));

    }

    @Test
    public void shouldAddNewAddressToAuthor() throws Exception {
        Address address = new Address("TestAddress",123);
        String addressJson = objectMapper.writeValueAsString(address);


        this.mockMvc.perform(put("/authors/1/address")
        .contentType(MediaType.APPLICATION_JSON)
        .content(addressJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.address.gateName", containsString("TestAddress")))
        .andExpect(jsonPath("$.address.gateNumber", is(123)));

        this.mockMvc.perform(get("/authors/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.address.gateName", containsString("TestAddress")))
        .andExpect(jsonPath("$.address.gateNumber", is(123)));
        
    }

    // Address address1 = new Address("Grimseidvegen", 435);
    @Test
    public void shouldAddExistingAddressToAuthor() throws Exception {
        this.mockMvc.perform(put("/authors/2/address/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.address.gateName", containsString("Grimseidvegen")))
        .andExpect(jsonPath("$.address.gateNumber", is(435)));

        this.mockMvc.perform(get("/authors/2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.address.gateName", containsString("Grimseidvegen")))
        .andExpect(jsonPath("$.address.gateNumber", is(435)));
    }

    // new Author("WillBeDeleted", "delete@test.com")
    @Test
    public void shouldDeleteAuthor() throws Exception {
        this.mockMvc.perform(delete("/authors/3"))
        .andExpect(status().isOk());

    }
    
}
