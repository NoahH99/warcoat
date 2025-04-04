package com.noahhendrickson.warcoat.repository.discord;

import com.noahhendrickson.warcoat.entity.discord.Guild;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildRepository extends JpaRepository<Guild, Long> {
}
