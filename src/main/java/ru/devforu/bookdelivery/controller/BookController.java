package ru.devforu.bookdelivery.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.devforu.bookdelivery.entity.Book;
import ru.devforu.bookdelivery.entity.Library;
import ru.devforu.bookdelivery.repository.BookRepository;
import ru.devforu.bookdelivery.repository.LibraryRepository;

import java.util.*;

/**
 * @author Miklyaev Konstantin <a href="https://devforu.ru/">DevForU</a>
 */

@RestController
@RequestMapping("/api")
public class BookController {

    Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private LibraryRepository libraryRepository;

    @PostMapping("/libraries/{libraryId}/books")
    public ResponseEntity<Book> createBook(@PathVariable(value = "libraryId") Long libraryId,
                                                 @RequestBody Book bookRequest) {
        try{
            Optional<Library> libraryData = libraryRepository.findById(libraryId);
            if (libraryData.isPresent()) {
                Optional<Book> bookDataExist = bookRepository
                        .findByNameAndAuthorAndGenreAndLibrary(
                                bookRequest.getName(),
                                bookRequest.getAuthor(),
                                bookRequest.getGenre(),
                                libraryData.get())
                        .stream().findFirst();
                if (bookDataExist.isPresent()){
                    return new ResponseEntity<>(bookDataExist.get(), HttpStatus.OK);
                }

                bookRequest.setLibrary(libraryData.get());
                return new ResponseEntity<>(bookRepository.save(bookRequest), HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.warn("Insert book exception: {}",e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/books")
    public ResponseEntity<Map<String, Object>> getAllBooksPage(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            List<Book> books;
            Pageable paging = PageRequest.of(page, size);
            Page<Book> pageBooks;
            if (name != null && author == null && genre == null) {
                pageBooks = bookRepository.findByNameContaining(name,paging);
            }else if (name != null & author != null & genre == null) {
                pageBooks = bookRepository.findByNameContainingAndAuthorContaining(name,author,paging);
            }else if (name != null & author != null & genre != null) {
                pageBooks = bookRepository.findByNameContainingAndAuthorContainingAndGenre(name,author,genre,paging);
            }else if (name != null & author == null & genre != null) {
                pageBooks = bookRepository.findByNameContainingAndGenre(name,genre,paging);
            }else if (author != null & genre == null) {
                pageBooks = bookRepository.findByAuthorContaining(author,paging);
            }else if (author == null & genre != null) {
                pageBooks = bookRepository.findByGenre(genre,paging);
            }else if (author != null & genre != null) {
                pageBooks = bookRepository.findByAuthorContainingAndGenre(author,genre,paging);
            } else{
                pageBooks = bookRepository.findAll(paging);
            }
            books = pageBooks.getContent();

            if (books.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            Map<String, Object> response = new HashMap<>();
            response.put("books", books);
            response.put("currentPage", pageBooks.getNumber());
            response.put("totalItems", pageBooks.getTotalElements());
            response.put("totalPages", pageBooks.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            logger.warn("Get book exception: {}",e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") long id) {
        try {
            Optional<Book> bookData = bookRepository.findById(id);
            if (bookData.isPresent()) {
                return new ResponseEntity<>(bookData.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.warn("Get book by id exception: {}",e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/libraries/{libraryId}/books/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable("libraryId") Long libraryId,
                                           @PathVariable("id") long id,
                                           @RequestBody Book book) {
        try {
            Optional<Library> libraryData = libraryRepository.findById(libraryId);
            if (libraryData.isPresent()) {
                Optional<Book> bookDataExist = bookRepository
                        .findByNameAndAuthorAndGenreAndLibrary(
                                book.getName(),
                                book.getAuthor(),
                                book.getGenre(),
                                libraryData.get())
                        .stream().findFirst();
                if (bookDataExist.isPresent()){
                    return new ResponseEntity<>(bookDataExist.get(), HttpStatus.OK);
                }

                Optional<Book> bookData = bookRepository.findById(id);
                if (bookData.isPresent()) {
                    Book _book = bookData.get();
                    _book.setName(book.getName());
                    _book.setAuthor(book.getAuthor());
                    _book.setGenre(book.getGenre());
                    _book.setAvailable(book.isAvailable());
                    return new ResponseEntity<>(bookRepository.save(_book), HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.warn("Update book exception: {}",e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Book> updateBookAvailable(@PathVariable("id") long id,
                                                    @RequestParam() boolean available) {
        try {
            Optional<Book> bookData = bookRepository.findById(id);
            if (bookData.isPresent()) {
                Book _book = bookData.get();
                _book.setAvailable(available);
                return new ResponseEntity<>(bookRepository.save(_book), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.warn("Update available book exception: {}",e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<HttpStatus> deleteBooks(@PathVariable("id") long id) {
        try {
            Optional<Book> bookData = bookRepository.findById(id);
            if (bookData.isPresent()) {
                bookRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            logger.warn("Delete book exception: {}",e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
