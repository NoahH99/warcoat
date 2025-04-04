package com.noahhendrickson.warcoat.service.welcome;

import com.noahhendrickson.warcoat.entity.welcome.WelcomeRole;
import com.noahhendrickson.warcoat.repository.welcome.WelcomeRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WelcomeRoleService {

    private final WelcomeRoleRepository roleRepository;

    public void assignWelcomeRoles(Guild guild, User user) {
        log.info("Assigning welcome roles to user {} in guild {}", user.getId(), guild.getId());
        List<WelcomeRole> roles = roleRepository.findByGuildId(guild.getIdLong());
        log.debug("Found {} welcome roles for guild {}", roles.size(), guild.getId());

        for (WelcomeRole role : roles) {
            Role roleObj = guild.getRoleById(role.getRole().getId());

            if (roleObj != null) {
                log.debug("Assigning role {} to user {} in guild {}", roleObj.getId(), user.getId(), guild.getId());
                guild.addRoleToMember(user, roleObj).queue(
                        success -> log.debug("Successfully assigned role {} to user {} in guild {}", roleObj.getId(), user.getId(), guild.getId()),
                        error -> log.error("Failed to assign role {} to user {} in guild {}: {}", roleObj.getId(), user.getId(), guild.getId(), error.getMessage())
                );
            } else {
                log.warn("Role {} not found in guild {}", role.getRole().getId(), guild.getId());
            }
        }
    }
}
