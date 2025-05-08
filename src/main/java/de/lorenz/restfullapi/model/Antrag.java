package de.lorenz.restfullapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

}
