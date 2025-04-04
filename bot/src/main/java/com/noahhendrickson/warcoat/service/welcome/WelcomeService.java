package com.noahhendrickson.warcoat.service.welcome;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WelcomeService {

    private final WelcomeConfigurationService configurationService;
    private final WelcomeMessageService messageService;
    private final WelcomeRoleService roleService;

    public void handleMemberJoin(Guild guild, User user) {
        log.info("Member join event for user {} in guild {}", user.getId(), guild.getId());
        if (!isEnabled(guild)) {
            log.debug("Welcome plugin disabled for guild {}, ignoring join event", guild.getId());
            return;
        }

        configurationService.getConfiguration(guild).ifPresent(config -> {
            log.debug("Found welcome configuration for guild {}, processing join event", guild.getId());
            messageService.sendWelcomeMessage(guild, user, config);
            roleService.assignWelcomeRoles(guild, user);
        });
    }

    public void handleMemberRemove(Guild guild, User user) {
        log.info("Member remove event for user {} in guild {}", user.getId(), guild.getId());
        if (!isEnabled(guild)) {
            log.debug("Welcome plugin disabled for guild {}, ignoring remove event", guild.getId());
            return;
        }

        configurationService.getConfiguration(guild).ifPresent(config -> {
            log.debug("Found welcome configuration for guild {}, processing remove event", guild.getId());
            messageService.sendGoodbyeMessage(guild, user, config);
        });
    }

    public boolean isEnabled(Guild guild) {
        boolean enabled = configurationService.isEnabled(guild);
        log.debug("Welcome plugin status for guild {}: {}", guild.getId(), enabled ? "enabled" : "disabled");
        return enabled;
    }
}
