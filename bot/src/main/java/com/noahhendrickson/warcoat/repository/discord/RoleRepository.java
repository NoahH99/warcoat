package com.noahhendrickson.warcoat.repository.discord;

import com.noahhendrickson.warcoat.entity.discord.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findByGuildId(Long guildId);
}
