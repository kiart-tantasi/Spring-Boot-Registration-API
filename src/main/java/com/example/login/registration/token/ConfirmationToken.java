package com.example.login.registration.token;

import com.example.login.appuser.AppUser;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor // default constructor
@Entity
public class ConfirmationToken {

    @Id
    @SequenceGenerator(
            name = "token_sequence",
            sequenceName = "token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "token_sequence"
    )
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime confirmedAt;

    // TODO: SEARCH what is many-to-one
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name="app_user_id"
    )
    private AppUser appUser;

    // constructor
    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expireAt, AppUser appUser) {
        this.token = token;
        this.createdAt = createdAt;
        this.appUser = appUser;
    }
}
