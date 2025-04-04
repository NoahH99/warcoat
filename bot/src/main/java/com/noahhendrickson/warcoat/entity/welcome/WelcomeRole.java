package com.noahhendrickson.warcoat.entity.welcome;

import com.noahhendrickson.warcoat.entity.discord.Guild;
import com.noahhendrickson.warcoat.entity.discord.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "welcome_roles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WelcomeRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @ManyToOne
    @JoinColumn(name = "guild_id", nullable = false)
    private Guild guild;
    
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}