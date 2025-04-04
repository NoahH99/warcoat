CREATE TABLE plugin_commands
(
    id           SERIAL PRIMARY KEY,
    plugin_name  VARCHAR(50) NOT NULL,
    command_name VARCHAR(50) NOT NULL,
    guild_id     BIGINT      NOT NULL,
    enabled      BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (guild_id) REFERENCES guilds (id) ON DELETE CASCADE,
    UNIQUE (plugin_name, command_name, guild_id)
);
