package com.noahhendrickson.warcoat.entity.discord;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "member_roles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRole {

    @EmbeddedId
    private MemberRoleId id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "guild_id", referencedColumnName = "guild_id", insertable = false, updatable = false),
            @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    })
    private GuildMember guildMember;

    @ManyToOne
    @JoinColumn(name = "role_id", insertable = false, updatable = false)
    private Role role;
}