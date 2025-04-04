-- Welcome Plugin Schema

-- Main welcome configuration table (per guild)
CREATE TABLE welcome_configurations
(
    guild_id           BIGINT PRIMARY KEY,
    enabled            BOOLEAN   NOT NULL DEFAULT FALSE,
    dm                 BOOLEAN   NOT NULL DEFAULT FALSE,
    welcome_channel_id BIGINT,
    goodbye_channel_id BIGINT,
    created_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (guild_id) REFERENCES guilds (id) ON DELETE CASCADE
);

-- Welcome and goodbye messages table
CREATE TABLE welcome_messages
(
    id           SERIAL PRIMARY KEY,
    guild_id     BIGINT      NOT NULL,
    message_type VARCHAR(10) NOT NULL,
    message_text TEXT,
    embed        JSONB,
    enabled      BOOLEAN     NOT NULL DEFAULT TRUE,
    created_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (guild_id) REFERENCES guilds (id) ON DELETE CASCADE,
    UNIQUE (guild_id, message_type)
);

-- Roles to assign to new members
CREATE TABLE welcome_roles
(
    id         SERIAL PRIMARY KEY,
    guild_id   BIGINT    NOT NULL,
    role_id    BIGINT    NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (guild_id) REFERENCES guilds (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE,
    UNIQUE (guild_id, role_id)
);

-- Indexes for better query performance
CREATE INDEX idx_welcome_configs_guild_id ON welcome_configurations (guild_id);
CREATE INDEX idx_welcome_messages_guild_id ON welcome_messages (guild_id);
CREATE INDEX idx_welcome_roles_guild_id ON welcome_roles (guild_id);

-- Example of what embed JSONB might contain (for documentation)
COMMENT ON COLUMN welcome_messages.embed IS
    $$Example of embed JSONB structure:
{
  "title": "Welcome to the server!",
  "description": "We're glad to have you here, {user}!",
  "color": 7506394,
  "timestamp": "2023-04-03T18:43:31.000Z",
  "footer": {
    "text": "Member #{memberCount}",
    "icon_url": "https://example.com/icon.png"
  },
  "thumbnail": {
    "url": "{userAvatar}"
  },
  "author": {
    "name": "{server}",
    "icon_url": "{serverIcon}"
  },
  "fields": [
    {
      "name": "Useful Channels",
      "value": "<#123456789012345678> - rules\n<#123456789012345678> - announcements",
      "inline": true
    },
    {
      "name": "Member Count",
      "value": "{memberCount}",
      "inline": true
    }
  ]
}
$$;
