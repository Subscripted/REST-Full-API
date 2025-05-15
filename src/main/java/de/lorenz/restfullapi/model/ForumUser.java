package de.lorenz.restfullapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "forum_user")
public class ForumUser {

    @Id
    @Column(name = "user_id")
    private Long userId;

    private String username;
    private String email;
    private String password;
    private String rank;
    private LocalDateTime creation_date;

}
