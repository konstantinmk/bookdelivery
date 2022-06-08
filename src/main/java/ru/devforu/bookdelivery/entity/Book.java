package ru.devforu.bookdelivery.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Miklyaev Konstantin <a href="https://devforu.ru/">DevForU</a>
 */

@Entity
@Table(name = "books")
@ToString
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "book_generator")
    @Setter
    @Getter
    private long id;

    @Setter
    @Getter
    @NotBlank(message = "Name is mandatory")
    @Column(name = "name", nullable = false)
    private String name;
    
    @Setter
    @Getter
    @NotBlank(message = "Author is mandatory")
    @Column(name = "author", nullable = false)
    private String author;
    
    @Setter
    @Getter
    @NotBlank(message = "Genre is mandatory")
    @Column(name = "genre", nullable = false)
    private String genre;

    @Setter
    @Getter
    @NotNull(message = "Availability value is required")
    @Column(name = "available", nullable = false)
    private boolean available;

    @Setter
    @Getter
    @NotNull(message = "Library is mandatory")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "library_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JsonIgnore
    //@ToString.Exclude
    private Library library;

    public Book(String name, String author, String genre, boolean available, Library library) {
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.available = available;
        this.library = library;
    }

}
