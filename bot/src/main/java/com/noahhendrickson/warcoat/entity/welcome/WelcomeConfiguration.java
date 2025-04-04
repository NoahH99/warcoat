package com.noahhendrickson.warcoat.entity.welcome;

import com.noahhendrickson.warcoat.entity.discord.Guild;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "welcome_configurations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WelcomeConfiguration {
    @Id
    @Column(name = "guild_id")
    private Long guildId;

    @OneToOne
    @JoinColumn(name = "guild_id")
    @MapsId
    private Guild guild;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(nullable = false)
    private Boolean dm;

    @Column(name = "welcome_channel_id")
    private Long welcomeChannelId;

    @Column(name = "goodbye_channel_id")
    private Long goodbyeChannelId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}