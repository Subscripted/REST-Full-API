package de.lorenz.restfullapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "forum_entbannungs_antraege_chat")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long chatId;

    @ManyToOne
    @JoinColumn(name = "antrags_id")
    private Antrag antrag;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private ForumUser sender;

    @Column(name = "message_id")
    private Long messageId;

    private String message;
    private LocalDateTime time;
    private Boolean reported;

}
