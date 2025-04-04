package com.noahhendrickson.warcoat.repository.welcome;

import com.noahhendrickson.warcoat.entity.welcome.WelcomeConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WelcomeConfigurationRepository extends JpaRepository<WelcomeConfiguration, Long> {

    Optional<WelcomeConfiguration> findByGuildId(Long guildId);
}
