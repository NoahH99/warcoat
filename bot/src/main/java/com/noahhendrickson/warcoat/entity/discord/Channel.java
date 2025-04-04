package com.noahhendrickson.warcoat.entity.discord;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "channels")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Channel {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "guild_id")
    private Guild guild;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Short type;

    private Integer position;

    @Column(columnDefinition = "TEXT")
    private String topic;

    @Column(name = "is_nsfw")
    private Boolean isNsfw;

    @Column(name = "rate_limit_per_user")
    private Integer rateLimitPerUser;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Channel parent;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_message_id")
    private Long lastMessageId;

    private Integer flags;
}