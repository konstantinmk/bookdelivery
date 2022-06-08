package ru.devforu.bookdelivery.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author Miklyaev Konstantin <a href="https://devforu.ru/">DevForU</a>
 */

@Entity
@Table(name = "libraries")
@ToString
@NoArgsConstructor
public class Library {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "library_generator")
    @Setter
    @Getter
    private long id;

    @Setter
    @Getter
    @NotEmpty(message = "Name is mandatory")
    @Column(name = "name")
    private String name;
    
    @Setter
    @Getter
    @NotEmpty(message = "Address is mandatory")
    @Column(name = "address")
    private String address;

    public Library(String name, String address) {
        this.name = name;
        this.address = address;
    }
}