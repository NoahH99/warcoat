package com.noahhendrickson.warcoat.entity.discord;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuildMemberId implements Serializable {
    @Column(name = "guild_id")
    private Long guildId;

    @Column(name = "user_id")
    private Long userId;
}