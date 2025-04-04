package com.noahhendrickson.warcoat.entity.discord;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "guilds")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Guild {
    @Id
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(name = "icon_url")
    private String iconUrl;
    
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "member_count", nullable = false)
    private Integer memberCount;
    
    @Column(name = "premium_tier", nullable = false)
    private Short premiumTier;
    
    private String region;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "banner_url")
    private String bannerUrl;
    
    @Column(name = "system_channel_id")
    private Long systemChannelId;
    
    @Column(name = "rules_channel_id")
    private Long rulesChannelId;
    
    @Column(name = "public_updates_channel_id")
    private Long publicUpdatesChannelId;
    
    @Column(name = "is_large", nullable = false)
    private Boolean isLarge;
    
    @Column(name = "is_unavailable", nullable = false)
    private Boolean isUnavailable;
    
    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;
}