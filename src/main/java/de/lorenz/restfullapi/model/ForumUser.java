package de.lorenz.restfullapi.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "forum_user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ForumUser {

    @Id
    @Column(name = "user_id")
    Long userId;

    String username;
    String email;
    String password;
    String rank;
    LocalDateTime creation_date;
    @Column(name = "insert_date")
    LocalDateTime insertDate;
    @Column(name = "last_updated")
    LocalDateTime lastUpdated;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.insertDate = now;
        this.lastUpdated = now;
        this.creation_date = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }
}
