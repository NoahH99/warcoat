package com.noahhendrickson.warcoat.entity.discord;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "guild_members")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GuildMember {
    @EmbeddedId
    private GuildMemberId id;

    @ManyToOne
    @MapsId("guildId")
    @JoinColumn(name = "guild_id")
    private Guild guild;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(length = 32)
    private String nickname;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @Column(name = "premium_since")
    private LocalDateTime premiumSince;

    @Column(name = "communication_disabled_until")
    private LocalDateTime communicationDisabledUntil;

    @Column(name = "is_pending")
    private Boolean isPending;

    @Column(name = "is_deaf")
    private Boolean isDeaf;

    @Column(name = "is_mute")
    private Boolean isMute;
}