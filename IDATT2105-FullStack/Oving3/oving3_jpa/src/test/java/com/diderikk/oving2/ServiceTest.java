package com.diderikk.oving2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.diderikk.oving2.model.Address;
import com.diderikk.oving2.model.Author;
import com.diderikk.oving2.model.Book;
import com.diderikk.oving2.repository.AddressRepository;
import com.diderikk.oving2.repository.AuthorRepository;
import com.diderikk.oving2.repository.BookRepository;
import com.diderikk.oving2.service.AuthorService;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ServiceTest {
    @InjectMocks
    private AuthorService authorService;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private BookRepository bookRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        List<Author> authors = new ArrayList<>();

        Author author1 = new Author("Diderik Kramer1", "diderik.kramer@gmail.com");
        Author author2 = new Author("Didkra", "diderikk@stud.ntnu.no");
        Author author3 = new Author("WillBeDeleted", "delete@test.com");

        Book tempBook = new Book("WillBeDeleted",789);
        tempBook.setBookID(1);

        Optional<Author> author = Optional.of(new Author("Diderik", "diderik.kramer@gmail.com"));
        author.get().setAddress(new Address("WillBeDeleted", 456));
        author.get().getBooks().add(tempBook);

        authors.addAll(List.of(author1, author2, author3));

        Mockito.lenient().when(authorRepository.save(any(Author.class))).thenAnswer(new Answer<Author>() {
            public Author answer(InvocationOnMock invocation) {
                return (Author) invocation.getArguments()[0];
            }
        });


        Mockito.lenient().when(authorRepository.findAll()).thenReturn(authors);
        Mockito.lenient().when(authorRepository.findById(anyLong())).thenReturn(author);
        Mockito.lenient().when(addressRepository.findById(anyLong()))
        .thenReturn(Optional.of(new Address("TestGate", 123)));
        Mockito.lenient().when(bookRepository.findById(anyLong()))
        .thenReturn(Optional.of(new Book("TestTitle", 123)));
    }

    @Test
    public void shouldReturnList(){
        List<Author> list = authorService.getAuthors();
        System.out.println(list.get(0).getName());
        assertEquals("Diderik Kramer1", list.get(0).getName());
        assertEquals("Didkra", list.get(1).getName());

    }

    @Test
    public void shouldReturnAuthor(){
        Author author = authorService.getAuthor(0);
        verify(authorRepository).findById(anyLong());
        assertEquals("Diderik", author.getName());
        assertEquals("diderik.kramer@gmail.com", author.getEmail());
    }

    @Test
    public void shouldUpdateAuthor(){
        Author authorTemp = new Author("Test","test@test.com");

        Author answer = authorService.updateAuthor(0, authorTemp);
        verify(authorRepository).save(any(Author.class));
        verify(authorRepository).findById(anyLong());
        assertEquals("Test", answer.getName());
        assertEquals("test@test.com", answer.getEmail());
    }

    @Test
    public void shouldAddNewAddress(){
        Address address = new Address("TestGate", 123);

        Author answer = authorService.addNewAddress(0, address);
        verify(authorRepository).findById(anyLong());
        verify(authorRepository).save(any(Author.class));
        assertEquals("TestGate", answer.getAddress().getGateName());
        assertEquals(123, answer.getAddress().getGateNumber());
    }

    @Test
    public void shouldAddExistingAddress(){
        Author answer = authorService.addExistingAddress(0, 0);

        verify(addressRepository).findById(anyLong());
        verify(authorRepository).findById(anyLong());

        assertEquals("TestGate", answer.getAddress().getGateName());
        assertEquals(123, answer.getAddress().getGateNumber());
        assertEquals("Diderik", answer.getName());
    }

    @Test
    public void shouldAddNewBookToAuthor(){
        Book book = new Book("TestTitle", 123);
        Author answer = authorService.addBook(0, book);

        verify(authorRepository).findById(anyLong());
        verify(authorRepository).save(any(Author.class));
        verify(bookRepository).save(any(Book.class));

        assertEquals("TestTitle", answer.getBooks().get(1).getTitle());
        assertEquals(123, answer.getBooks().get(1).getPageAmount());
    }

    @Test
    public void shouldAddExistingBook(){
        Author answer = authorService.addExistingBook(0, 0);

        verify(authorRepository).findById(anyLong());
        verify(authorRepository).save(any(Author.class));
        verify(bookRepository).findById(anyLong());

        assertEquals("TestTitle", answer.getBooks().get(1).getTitle());
        assertEquals(123, answer.getBooks().get(1).getPageAmount());
    }

    @Test
    public void shouldRemoveBook(){
        Author answer = authorService.getAuthor(0);
        assertEquals(1, answer.getBooks().size());

        answer = authorService.removeBook(0, 1);
        assertEquals(0, answer.getBooks().size());

    }

    @Test
    public void shouldRemoveAddress(){
        Author answer = authorService.getAuthor(0);
        assertNotNull(answer.getAddress());

        answer = authorService.removeAddress(0);
        assertNull(answer.getAddress());

    }


}
