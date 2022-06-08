package ru.devforu.bookdelivery.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.devforu.bookdelivery.entity.Book;
import ru.devforu.bookdelivery.entity.Library;

import java.util.List;

/**
 * @author Miklyaev Konstantin <a href="https://devforu.ru/">DevForU</a>
 */

@Repository     
public interface BookRepository  extends JpaRepository<Book, Long> {
    List<Book> findByNameContaining(String name);
    List<Book> findByNameAndAuthorAndGenreAndLibrary(
            String name,
            String author,
            String genre,
            Library library);

    Page<Book> findByNameContaining(
            String name,
            Pageable pageable);

    Page<Book> findByAuthorContaining(
            String author,
            Pageable pageable);

    Page<Book> findByGenre(
            String author,
            Pageable pageable);

    Page<Book> findByNameContainingAndAuthorContaining(
            String name,
            String author,
            Pageable pageable);

    Page<Book> findByNameContainingAndGenre(
            String name,
            String genre,
            Pageable pageable);

    Page<Book> findByAuthorContainingAndGenre(
            String author,
            String genre,
            Pageable pageable);

    Page<Book> findByNameContainingAndAuthorContainingAndGenre(
            String name,
            String author,
            String genre,
            Pageable pageable);

}
