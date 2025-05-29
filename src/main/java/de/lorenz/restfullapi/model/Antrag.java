package de.lorenz.restfullapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "forum_entbannungs_antraege")
public class Antrag {

    @Id
    @Column(name = "antrags_id")
    private Long antragsId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private ForumUser user;

    @ManyToOne
    @JoinColumn(name = "teamler_id")
    private ForumUser teamler;

    @Column(name = "status")
    private boolean status;

    @Column(name = "antrag_title")
    private String title;

    @Column(name = "insert_date")
    private LocalDateTime insertDate;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;


    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.insertDate = now;
        this.lastUpdated = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }
}
