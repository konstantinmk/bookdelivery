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
import ru.devforu.bookdelivery.entity.BookRequest;
import ru.devforu.bookdelivery.repository.BookRequestRepository;

import java.util.*;

/**
 * @author Miklyaev Konstantin <a href="https://devforu.ru/">DevForU</a>
 */

@RestController
@RequestMapping("/api")
public class BookRequestController {

    Logger logger = LoggerFactory.getLogger(BookRequestController.class);
    @Autowired
    BookRequestRepository requestRepository;

    @PostMapping("/requests")
    public ResponseEntity<BookRequest> createRequests(@RequestBody BookRequest request) {
        try {
            BookRequest _request = requestRepository
                    .save(new BookRequest(
                            request.getBookId(),
                            request.getTakingDate(),
                            request.getReturnDate(),
                            request.getPhone()));
            return new ResponseEntity<>(_request, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.warn("Insert request exception: {}",e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/requests")
    public ResponseEntity<Map<String, Object>> getAllRequest(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        try {
            Pageable paging = PageRequest.of(page, size);
            Page<BookRequest> pageRequests = requestRepository.findAll(paging);
            List<BookRequest> requests = pageRequests.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put("requests", requests);
            response.put("currentPage", pageRequests.getNumber());
            response.put("totalItems", pageRequests.getTotalElements());
            response.put("totalPages", pageRequests.getTotalPages());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            logger.warn("Get request exception: {}",e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/requests/{id}")
    public ResponseEntity<BookRequest> getBookRequestById(@PathVariable("id") long id) {
        try {
            Optional<BookRequest> requestData = requestRepository.findById(id);
            if (requestData.isPresent()) {
                return new ResponseEntity<>(requestData.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.warn("Get by id request exception: {}",e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/requests/{id}")
    public ResponseEntity<BookRequest> updateBookRequest(@PathVariable("id") long id, @RequestBody BookRequest request) {
        try {
            Optional<BookRequest> requestData = requestRepository.findById(id);
            if (requestData.isPresent()) {
                BookRequest _request = requestData.get();
                _request.setBookId(request.getBookId());
                _request.setTakingDate(request.getTakingDate());
                _request.setReturnDate(request.getReturnDate());
                _request.setPhone(request.getPhone());
                return new ResponseEntity<>(requestRepository.save(_request), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.warn("Update request exception: {}",e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/requests/{id}")
    public ResponseEntity<HttpStatus> deleteRequest(@PathVariable("id") long id) {
        try {
            Optional<BookRequest> requestData = requestRepository.findById(id);
            if (requestData.isPresent()) {
                requestRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

        } catch (Exception e) {
            logger.warn("Delete request exception: {}",e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
