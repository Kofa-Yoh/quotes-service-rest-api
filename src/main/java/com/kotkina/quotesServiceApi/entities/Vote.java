package com.kotkina.quotesServiceApi.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quote_id", nullable = false)
    @Setter
    private Quote quote;

    @Column(nullable = false)
    @Setter
    private byte assessment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @Setter
    private User user;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @Builder
    public Vote(Quote quote, byte assessment, User user) {
        this.quote = quote;
        this.assessment = assessment;
        this.user = user;
    }
}
