package com.noahhendrickson.warcoat.repository.plugin;

import com.noahhendrickson.warcoat.entity.plugin.PluginCommand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PluginCommandRepository extends JpaRepository<PluginCommand, Integer> {

    List<PluginCommand> findByGuildId(Long guildId);

    List<PluginCommand> findByPluginName(String pluginName);

    List<PluginCommand> findByGuildIdAndPluginName(Long guildId, String pluginName);

    Optional<PluginCommand> findByPluginNameAndCommandNameAndGuildId(String pluginName, String commandName, Long guildId);

    List<PluginCommand> findByGuildIdAndEnabled(Long guildId, Boolean enabled);

    List<PluginCommand> findByPluginNameAndEnabled(String pluginName, Boolean enabled);

    List<PluginCommand> findByGuildIdAndPluginNameAndEnabled(Long guildId, String pluginName, Boolean enabled);

    void deleteByPluginNameAndCommandNameAndGuildId(String pluginName, String commandName, Long guildId);
}
