package ru.devforu.bookdelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.devforu.bookdelivery.entity.BookRequest;
import ru.devforu.bookdelivery.entity.Library;

import java.util.List;

/**
 * @author Miklyaev Konstantin <a href="https://devforu.ru/">DevForU</a>
 */

@Repository     
public interface BookRequestRepository extends JpaRepository<BookRequest, Long> {
}
