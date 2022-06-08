package ru.devforu.bookdelivery.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.devforu.bookdelivery.entity.Library;
import ru.devforu.bookdelivery.repository.LibraryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Miklyaev Konstantin <a href="https://devforu.ru/">DevForU</a>
 */

@RestController
@RequestMapping("/api")
public class LibraryController {

    Logger logger = LoggerFactory.getLogger(LibraryController.class);
    @Autowired
    LibraryRepository libraryRepository;

    @PostMapping("/libraries")
    public ResponseEntity<Library> createLibrary(@RequestBody Library library) {
        try {
            Optional<Library> libraryData = libraryRepository
                    .findByNameAndAddress(library.getName(),library.getAddress())
                    .stream().findFirst();
            if (libraryData.isPresent()) {
                return new ResponseEntity<>(libraryData.get(), HttpStatus.OK);
            } else {
                Library _library = libraryRepository
                        .save(new Library(library.getName(), library.getAddress()));
                return new ResponseEntity<>(_library, HttpStatus.CREATED);
            }
        } catch (Exception e) {
            logger.warn("Insert library exception: {}",e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/libraries")
    public ResponseEntity<List<Library>> getAllLibraries(@RequestParam(required = false) String name) {
        try {
            List<Library> libraries = new ArrayList<>();
            if (name == null)
                libraries.addAll(libraryRepository.findAll());
            else
                libraries.addAll(libraryRepository.findByNameContaining(name));
            if (libraries.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(libraries, HttpStatus.OK);
        } catch (Exception e) {
            logger.warn("Get library exception: {}",e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/libraries/{id}")
    public ResponseEntity<Library> getLibraryById(@PathVariable("id") long id) {
        try {
            Optional<Library> libraryData = libraryRepository.findById(id);
            if (libraryData.isPresent()) {
                return new ResponseEntity<>(libraryData.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.warn("Get library by id exception: {}",e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/libraries/{id}")
    public ResponseEntity<Library> updateLibrary(@PathVariable("id") long id, @RequestBody Library library) {
        try {
            Optional<Library> libraryData = libraryRepository.findById(id);
            if (libraryData.isPresent()) {
                Optional<Library> libraryDataExist = libraryRepository
                        .findByNameAndAddress(library.getName(),library.getAddress())
                        .stream().findFirst();
                if (libraryDataExist.isPresent()) {
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
                }
                Library _library = libraryData.get();
                _library.setName(library.getName());
                _library.setAddress(library.getAddress());
                return new ResponseEntity<>(libraryRepository.save(_library), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.warn("Update library exception: {}",e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/libraries/{id}")
    public ResponseEntity<HttpStatus> deleteLibraries(@PathVariable("id") long id) {
        try {
            Optional<Library> libraryData = libraryRepository.findById(id);
            if (libraryData.isPresent()) {
                libraryRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            logger.warn("Delete library exception: {}",e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
