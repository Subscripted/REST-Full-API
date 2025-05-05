package de.lorenz.restfullapi.model;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "forum_user")
public class ForumUser {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String username;
    private String email;
    private String password;
    private String rank;

}
