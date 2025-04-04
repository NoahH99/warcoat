package com.noahhendrickson.warcoat.entity.discord;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "guild_id", nullable = false)
    private Guild guild;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer color;

    @Column(nullable = false)
    private Integer position;

    @Column(nullable = false)
    private Long permissions;

    @Column(name = "is_hoisted", nullable = false)
    private Boolean isHoisted;

    @Column(name = "is_managed", nullable = false)
    private Boolean isManaged;

    @Column(name = "is_mentionable", nullable = false)
    private Boolean isMentionable;

    @Column(name = "icon_url")
    private String iconUrl;

    @Column(name = "unicode_emoji", length = 100)
    private String unicodeEmoji;
}