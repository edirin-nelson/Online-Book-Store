package com.interswitch.security;

import com.interswitch.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "jwt_token")
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer token_id;

    @Column(unique = true)
    public String token;

    public boolean revoked;

    public boolean expired;

    private String refreshToken;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    public User user;

    private Date generatedAt;

    private Date expiresAt;

    private Date refreshedAt;

}
