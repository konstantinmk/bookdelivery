package ru.devforu.bookdelivery.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Miklyaev Konstantin <a href="https://devforu.ru/">DevForU</a>
 */

@Entity
@Table(name = "requests")
@ToString
@NoArgsConstructor
public class BookRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "request_generator")
    @Setter
    @Getter
    private long id;

    @Setter
    @Getter
    @NotNull(message = "Book is mandatory")
    private long bookId;

    @Setter
    @Getter
    @NotNull(message = "Taking date is mandatory")
    @Column(name = "taking_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date takingDate;

    @Setter
    @Getter
    @NotNull(message = "Return date is mandatory")
    @Column(name = "return_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date returnDate;

    @Setter
    @Getter
    @NotBlank(message = "Phone number is mandatory")
    @Column(name = "phone", nullable = false)
    private String phone;

    public BookRequest(long book_id, Date taking_date, Date return_date, String phone) {
        this.bookId = book_id;
        this.takingDate = taking_date;
        this.returnDate = return_date;
        this.phone = phone;
    }
}
