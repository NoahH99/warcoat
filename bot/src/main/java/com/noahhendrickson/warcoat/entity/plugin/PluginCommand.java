package com.noahhendrickson.warcoat.entity.plugin;

import com.noahhendrickson.warcoat.entity.discord.Guild;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "plugin_commands",
        uniqueConstraints = @UniqueConstraint(columnNames = {"plugin_name", "command_name", "guild_id"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PluginCommand {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "plugin_name", nullable = false, length = 50)
    private String pluginName;

    @Column(name = "command_name", nullable = false, length = 50)
    private String commandName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guild_id", nullable = false)
    private Guild guild;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;

        if (enabled == null) {
            enabled = true;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}