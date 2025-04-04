package com.noahhendrickson.warcoat.entity.discord;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private Long id;
    
    @Column(nullable = false, length = 32)
    private String username;
    
    @Column(length = 4)
    private String discriminator;
    
    @Column(name = "global_name", length = 100)
    private String globalName;
    
    @Column(name = "avatar_url")
    private String avatarUrl;
    
    @Column(name = "is_bot", nullable = false)
    private Boolean isBot;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(length = 10)
    private String locale;
    
    @Column(length = 100)
    private String email;
    
    private Integer flags;
    
    @Column(name = "premium_type")
    private Short premiumType;
    
    @Column(name = "mfa_enabled")
    private Boolean mfaEnabled;
    
    private Boolean verified;
    
    @Column(name = "is_system")
    private Boolean isSystem;
    
    @Column(name = "accent_color")
    private Integer accentColor;
    
    @Column(name = "banner_url")
    private String bannerUrl;
}