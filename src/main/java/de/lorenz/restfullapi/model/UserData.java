package de.lorenz.restfullapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Spielerdaten")
public class UserData {

    @Id
    @Column(name = "uuid", nullable = false, length = 36)
    @Getter
    @Setter
    private String uuid;

    @Column(nullable = false)
    @Getter
    @Setter
    private String name;

    @Column(nullable = false)
    @Getter
    @Setter
    private String ip;

    @Column(nullable = false)
    @Getter
    @Setter
    private boolean verified;

    @Column(nullable = false)
    @Getter
    @Setter
    private String discord_userid;

    @Column(nullable = false)
    @Getter
    @Setter
    private String verified_date;

    @Column(nullable = false)
    @Getter
    @Setter
    private int spielzeit;

    @Column(nullable = false)
    @Getter
    @Setter
    private int coins;


}
