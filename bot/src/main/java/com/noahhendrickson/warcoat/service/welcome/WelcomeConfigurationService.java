package com.noahhendrickson.warcoat.service.welcome;

import com.noahhendrickson.warcoat.entity.welcome.WelcomeConfiguration;
import com.noahhendrickson.warcoat.repository.welcome.WelcomeConfigurationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WelcomeConfigurationService {

    private final WelcomeConfigurationRepository configurationRepository;

    public boolean isEnabled(Guild guild) {
        boolean enabled = configurationRepository.findByGuildId(guild.getIdLong())
                .map(WelcomeConfiguration::getEnabled)
                .orElse(false);
        log.debug("Checking if welcome plugin is enabled for guild {}: {}", guild.getId(), enabled);
        return enabled;
    }

    public Optional<WelcomeConfiguration> getConfiguration(Guild guild) {
        Optional<WelcomeConfiguration> config = configurationRepository.findByGuildId(guild.getIdLong());

        if (config.isPresent()) log.debug("Found welcome configuration for guild {}", guild.getId());
        else log.debug("No welcome configuration found for guild {}", guild.getId());

        return config;
    }
}
