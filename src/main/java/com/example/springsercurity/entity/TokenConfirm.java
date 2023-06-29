package com.example.springsercurity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="token_confirm")
public class TokenConfirm {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name="token")
    private String token;
    @Column(name="createAt ")
    private LocalDateTime createdAt;
    @Column(name="expiredAt")
    private LocalDateTime expiredAt;
    @Column(name="confirmAt ")
    private LocalDateTime confirmAt;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

}
