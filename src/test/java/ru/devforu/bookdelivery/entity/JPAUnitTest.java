package ru.devforu.bookdelivery.entity;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ru.devforu.bookdelivery.repository.BookRepository;
import ru.devforu.bookdelivery.repository.LibraryRepository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


/**
 * @author Miklyaev Konstantin <a href="https://devforu.ru/">DevForU</a>
 */

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class JPAUnitTest {
    Logger logger = LoggerFactory.getLogger(JPAUnitTest.class);
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    LibraryRepository library_repository;

    @Autowired
    BookRepository book_repository;

    @Test
    public void no_library_if_repository_is_empty() {
        Iterable<Library> library = library_repository.findAll();
        assertThat(library).isEmpty();
    }

    @Test
    public void no_books_if_repository_is_empty() {
        Iterable<Book> books = book_repository.findAll();
        assertThat(books).isEmpty();
    }

    @Test
    public void store_a_library() {
        Library libraries = library_repository.save(new Library("First","Address"));
        assertThat(libraries).hasFieldOrPropertyWithValue("name", "First");
        assertThat(libraries).hasFieldOrPropertyWithValue("address", "Address");

    }
    @Test
    public void check_a_library() {
        Library library_first = new Library("First","Address");
        entityManager.persist(library_first);
        Library library_second = new Library("Second","Address");
        entityManager.persist(library_second);

        Iterable<Library> libraries = library_repository.findAll();
        assertThat(libraries).hasSize(2).contains(library_first, library_second);
    }
    @Test
    public void check_a_books() {
        Library library_first = new Library("First","Address One");
        entityManager.persist(library_first);
        Library library_second = new Library("Second","Address Two");
        entityManager.persist(library_second);
        Book book_one = new Book(
                "First Book",
                "Writer One",
                "Tale",
                true,
                library_first);
        entityManager.persist(book_one);
        Book book_two = new Book(
                "Second Book",
                "Writer Two",
                "Story",
                true,
                library_second);
        entityManager.persist(book_two);
        Book book_three = new Book(
                "Third Book",
                "Writer Two",
                "Poetry",
                false,
                library_second);
        entityManager.persist(book_three);
        Book book_four = new Book(
                "Fourth Book",
                "Writer Three",
                "Tale",
                true,
                library_first);
        entityManager.persist(book_four);
        Iterable<Book> books = book_repository.findAll();


        for(Book curr : books)
        {
            logger.info(curr.toString());
        }

        assertThat(books).hasSize(4).contains(book_one, book_two,book_three,book_four);
    }

}
