package uz.company.app5managment.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Turnstile {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(optional = false)
    private User user;

    @Column(nullable = false)
    private Timestamp timestamp = new Timestamp(System.currentTimeMillis());

    private boolean enter;

}
