-- Discord Entities Schema

-- Guilds (Discord Servers)
CREATE TABLE guilds (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    icon_url VARCHAR(255),
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    member_count INT NOT NULL DEFAULT 0,
    premium_tier SMALLINT NOT NULL DEFAULT 0,
    region VARCHAR(50),
    description TEXT,
    banner_url VARCHAR(255),
    system_channel_id BIGINT,
    rules_channel_id BIGINT,
    public_updates_channel_id BIGINT,
    is_large BOOLEAN NOT NULL DEFAULT FALSE,
    is_unavailable BOOLEAN NOT NULL DEFAULT FALSE,
    joined_at TIMESTAMP NOT NULL
);

-- Users
CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    username VARCHAR(32) NOT NULL,
    discriminator VARCHAR(4),
    global_name VARCHAR(100),
    avatar_url VARCHAR(255),
    is_bot BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL,
    locale VARCHAR(10),
    email VARCHAR(100),
    flags INT,
    premium_type SMALLINT DEFAULT 0,
    mfa_enabled BOOLEAN DEFAULT FALSE,
    verified BOOLEAN DEFAULT FALSE,
    is_system BOOLEAN DEFAULT FALSE,
    accent_color INT,
    banner_url VARCHAR(255)
);

-- Guild Members (Users in specific Guilds)
CREATE TABLE guild_members (
    guild_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    nickname VARCHAR(32),
    avatar_url VARCHAR(255),
    joined_at TIMESTAMP NOT NULL,
    premium_since TIMESTAMP,
    communication_disabled_until TIMESTAMP,
    is_pending BOOLEAN DEFAULT FALSE,
    is_deaf BOOLEAN DEFAULT FALSE,
    is_mute BOOLEAN DEFAULT FALSE,
    PRIMARY KEY (guild_id, user_id),
    FOREIGN KEY (guild_id) REFERENCES guilds(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Roles
CREATE TABLE roles (
    id BIGINT PRIMARY KEY,
    guild_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    color INT NOT NULL DEFAULT 0,
    position INT NOT NULL DEFAULT 0,
    permissions BIGINT NOT NULL DEFAULT 0,
    is_hoisted BOOLEAN NOT NULL DEFAULT FALSE,
    is_managed BOOLEAN NOT NULL DEFAULT FALSE,
    is_mentionable BOOLEAN NOT NULL DEFAULT FALSE,
    icon_url VARCHAR(255),
    unicode_emoji VARCHAR(100),
    FOREIGN KEY (guild_id) REFERENCES guilds(id) ON DELETE CASCADE
);

-- Member Roles
CREATE TABLE member_roles (
    guild_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (guild_id, user_id, role_id),
    FOREIGN KEY (guild_id, user_id) REFERENCES guild_members(guild_id, user_id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE CASCADE
);

-- Channels
CREATE TABLE channels (
    id BIGINT PRIMARY KEY,
    guild_id BIGINT,
    name VARCHAR(100) NOT NULL,
    type SMALLINT NOT NULL,
    position INT,
    topic TEXT,
    is_nsfw BOOLEAN DEFAULT FALSE,
    rate_limit_per_user INT DEFAULT 0,
    parent_id BIGINT,
    created_at TIMESTAMP NOT NULL,
    last_message_id BIGINT,
    flags INT,
    FOREIGN KEY (guild_id) REFERENCES guilds(id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES channels(id) ON DELETE SET NULL
);

-- Messages
CREATE TABLE messages (
    id BIGINT PRIMARY KEY,
    channel_id BIGINT NOT NULL,
    author_id BIGINT NOT NULL,
    content TEXT,
    timestamp TIMESTAMP NOT NULL,
    edited_timestamp TIMESTAMP,
    tts BOOLEAN NOT NULL DEFAULT FALSE,
    mention_everyone BOOLEAN NOT NULL DEFAULT FALSE,
    pinned BOOLEAN NOT NULL DEFAULT FALSE,
    type SMALLINT NOT NULL,
    flags INT,
    reference_message_id BIGINT,
    FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (reference_message_id) REFERENCES messages(id) ON DELETE SET NULL
);

-- Indexes for better query performance
CREATE INDEX idx_members_guild_id ON guild_members(guild_id);
CREATE INDEX idx_members_user_id ON guild_members(user_id);
CREATE INDEX idx_roles_guild_id ON roles(guild_id);
CREATE INDEX idx_member_roles_guild_id ON member_roles(guild_id);
CREATE INDEX idx_member_roles_user_id ON member_roles(user_id);
CREATE INDEX idx_channels_guild_id ON channels(guild_id);
CREATE INDEX idx_messages_channel_id ON messages(channel_id);
CREATE INDEX idx_messages_author_id ON messages(author_id);
