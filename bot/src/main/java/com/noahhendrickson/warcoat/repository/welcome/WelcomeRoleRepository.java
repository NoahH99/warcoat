package com.noahhendrickson.warcoat.repository.welcome;

import com.noahhendrickson.warcoat.entity.welcome.WelcomeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WelcomeRoleRepository extends JpaRepository<WelcomeRole, Long> {

    List<WelcomeRole> findByGuildId(Long guildId);

    Optional<WelcomeRole> findByGuildIdAndRoleId(Long guildId, Long roleId);

    void deleteByGuildIdAndRoleId(Long guildId, Long roleId);
}
