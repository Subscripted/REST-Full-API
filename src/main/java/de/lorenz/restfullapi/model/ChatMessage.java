package de.lorenz.restfullapi.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "forum_entbannungs_antraege_chat")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    Long chatId;

    @ManyToOne
    @JoinColumn(name = "antrags_id")
    Antrag antrag;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    ForumUser sender;

    @Column(name = "message_id")
    Long messageId;

    String message;
    LocalDateTime time;
    Boolean reported;

    @Column(name = "insert_date")
    LocalDateTime insertDate;
    @Column(name = "last_updated")
    LocalDateTime lastUpdated;



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
