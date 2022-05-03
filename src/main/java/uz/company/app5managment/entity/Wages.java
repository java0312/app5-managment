package uz.company.app5managment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Wages {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne
    private User user;

    @Column(nullable = false)
    private double price;

    @CreatedDate
    private Date givenDate;

}
