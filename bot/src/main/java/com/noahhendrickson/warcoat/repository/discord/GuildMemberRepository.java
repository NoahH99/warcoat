package com.noahhendrickson.warcoat.repository.discord;

import com.noahhendrickson.warcoat.entity.discord.GuildMember;
import com.noahhendrickson.warcoat.entity.discord.GuildMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuildMemberRepository extends JpaRepository<GuildMember, GuildMemberId> {

    List<GuildMember> findByGuildId(Long guildId);

    List<GuildMember> findByUserId(Long userId);
}
