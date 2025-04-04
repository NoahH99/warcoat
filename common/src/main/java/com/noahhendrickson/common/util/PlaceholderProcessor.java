package com.noahhendrickson.common.util;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceholderProcessor {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\{([^.}]+)(?:\\.([^}]+))?}");

    public static String process(String template, PlaceholderContext context) {
        if (template == null) return null;

        Map<String, Map<String, Function<PlaceholderContext, String>>> placeholders = createPlaceholderMap();

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(template);
        StringBuilder result = new StringBuilder();

        while (matcher.find()) {
            String category = matcher.group(1);
            String property = matcher.group(2);

            String replacement = matcher.group();
            Map<String, Function<PlaceholderContext, String>> categoryMap = placeholders.get(category);

            if (categoryMap != null) {
                Function<PlaceholderContext, String> valueProvider = categoryMap.get(Objects.requireNonNullElse(property, "default"));

                if (valueProvider != null) replacement = valueProvider.apply(context);
            }

            matcher.appendReplacement(result, Matcher.quoteReplacement(replacement != null ? replacement : ""));
        }

        matcher.appendTail(result);

        return result.toString();
    }

    /**
     * Process placeholders with guild and user context (convenience method)
     */
    public static String process(String template, Guild guild, User user) {
        if (template == null) return null;

        Member member = guild != null && user != null ? guild.getMember(user) : null;
        PlaceholderContext context = PlaceholderContext.builder()
                .guild(guild)
                .user(user)
                .member(member)
                .build();

        return process(template, context);
    }

    /**
     * Process placeholders with guild, channel and user context (convenience method)
     */
    public static String process(String template, Guild guild, TextChannel channel, User user) {
        if (template == null) return null;

        Member member = guild != null && user != null ? guild.getMember(user) : null;
        PlaceholderContext context = PlaceholderContext.builder()
                .guild(guild)
                .user(user)
                .member(member)
                .channel(channel)
                .build();

        return process(template, context);
    }

    private static Map<String, Map<String, Function<PlaceholderContext, String>>> createPlaceholderMap() {
        Map<String, Map<String, Function<PlaceholderContext, String>>> placeholders = new HashMap<>();

        // Server placeholders
        Map<String, Function<PlaceholderContext, String>> serverPlaceholders = new HashMap<>();
        serverPlaceholders.put("default", ctx -> ctx.guild != null ? ctx.guild.getName() : "");
        serverPlaceholders.put("name", ctx -> ctx.guild != null ? ctx.guild.getName() : "");
        serverPlaceholders.put("id", ctx -> ctx.guild != null ? ctx.guild.getId() : "");
        serverPlaceholders.put("icon_url", ctx -> ctx.guild != null ? ctx.guild.getIconUrl() : "");
        serverPlaceholders.put("member_count", ctx -> ctx.guild != null ? String.valueOf(ctx.guild.getMemberCount()) : "0");
        serverPlaceholders.put("owner", ctx -> {
            if (ctx.guild == null) return "";
            User owner = ctx.guild.getOwner() != null ? ctx.guild.getOwner().getUser() : null;
            return owner != null ? owner.getName() : "";
        });
        serverPlaceholders.put("owner_id", ctx -> {
            if (ctx.guild == null) return "";
            return ctx.guild.getOwnerId();
        });
        serverPlaceholders.put("boost_count", ctx -> ctx.guild != null ? String.valueOf(ctx.guild.getBoostCount()) : "0");
        serverPlaceholders.put("boost_tier", ctx -> {
            if (ctx.guild == null) return "";
            return ctx.guild.getBoostTier().name();
        });
        placeholders.put("server", serverPlaceholders);

        // User placeholders
        Map<String, Function<PlaceholderContext, String>> userPlaceholders = new HashMap<>();
        userPlaceholders.put("default", ctx -> ctx.user != null ? ctx.user.getName() : "");
        userPlaceholders.put("name", ctx -> ctx.user != null ? ctx.user.getName() : "");
        userPlaceholders.put("id", ctx -> ctx.user != null ? ctx.user.getId() : "");
        userPlaceholders.put("mention", ctx -> ctx.user != null ? ctx.user.getAsMention() : "");
        userPlaceholders.put("discriminator", ctx -> ctx.user != null ? ctx.user.getDiscriminator() : "");
        userPlaceholders.put("global_name", ctx -> ctx.user != null ? ctx.user.getGlobalName() : "");
        userPlaceholders.put("avatar_url", ctx -> ctx.user != null ? ctx.user.getEffectiveAvatarUrl() : "");
        userPlaceholders.put("created_time", ctx -> {
            if (ctx.user == null) return "";
            return ctx.user.getTimeCreated().toString();
        });
        userPlaceholders.put("is_bot", ctx -> ctx.user != null ? String.valueOf(ctx.user.isBot()) : "false");
        placeholders.put("user", userPlaceholders);

        // Member placeholders (user in guild context)
        Map<String, Function<PlaceholderContext, String>> memberPlaceholders = new HashMap<>();
        memberPlaceholders.put("default", ctx -> ctx.member != null ? ctx.member.getEffectiveName() : "");
        memberPlaceholders.put("nickname", ctx -> ctx.member != null ? ctx.member.getNickname() : "");
        memberPlaceholders.put("effective_name", ctx -> ctx.member != null ? ctx.member.getEffectiveName() : "");
        memberPlaceholders.put("color", ctx -> {
            if (ctx.member == null || ctx.member.getColor() == null) return "";
            return "#" + Integer.toHexString(ctx.member.getColorRaw()).toUpperCase();
        });
        memberPlaceholders.put("color_raw", ctx -> ctx.member != null ? String.valueOf(ctx.member.getColorRaw()) : "0");
        memberPlaceholders.put("joined_time", ctx -> {
            if (ctx.member == null) return "";
            return ctx.member.getTimeJoined().toString();
        });
        memberPlaceholders.put("boosting_since", ctx -> {
            if (ctx.member == null || ctx.member.getTimeBoosted() == null) return "";
            return ctx.member.getTimeBoosted().toString();
        });
        memberPlaceholders.put("roles", ctx -> {
            if (ctx.member == null) return "";
            return ctx.member.getRoles().stream()
                    .map(Role::getName)
                    .reduce((a, b) -> a + ", " + b)
                    .orElse("");
        });
        memberPlaceholders.put("highest_role", ctx -> {
            if (ctx.member == null || ctx.member.getRoles().isEmpty()) return "";
            return ctx.member.getRoles().getFirst().getName();
        });
        placeholders.put("member", memberPlaceholders);

        // Channel placeholders
        Map<String, Function<PlaceholderContext, String>> channelPlaceholders = new HashMap<>();
        channelPlaceholders.put("default", ctx -> ctx.channel != null ? ctx.channel.getName() : "");
        channelPlaceholders.put("name", ctx -> ctx.channel != null ? ctx.channel.getName() : "");
        channelPlaceholders.put("id", ctx -> ctx.channel != null ? ctx.channel.getId() : "");
        channelPlaceholders.put("mention", ctx -> ctx.channel != null ? ctx.channel.getAsMention() : "");
        channelPlaceholders.put("topic", ctx -> ctx.channel != null ? ctx.channel.getTopic() : "");
        channelPlaceholders.put("created_time", ctx -> {
            if (ctx.channel == null) return "";
            return ctx.channel.getTimeCreated().toString();
        });
        channelPlaceholders.put("position", ctx -> ctx.channel != null ? String.valueOf(ctx.channel.getPosition()) : "0");
        channelPlaceholders.put("nsfw", ctx -> ctx.channel != null ? String.valueOf(ctx.channel.isNSFW()) : "false");
        placeholders.put("channel", channelPlaceholders);

        return placeholders;
    }

    public static class PlaceholderContext {

        private final Guild guild;
        private final User user;
        private final Member member;
        private final TextChannel channel;

        private PlaceholderContext(Guild guild, User user, Member member, TextChannel channel) {
            this.guild = guild;
            this.user = user;
            this.member = member;
            this.channel = channel;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {

            private Guild guild;
            private User user;
            private Member member;
            private TextChannel channel;

            public Builder guild(Guild guild) {
                this.guild = guild;
                return this;
            }

            public Builder user(User user) {
                this.user = user;
                return this;
            }

            public Builder member(Member member) {
                this.member = member;
                return this;
            }

            public Builder channel(TextChannel channel) {
                this.channel = channel;
                return this;
            }

            public PlaceholderContext build() {
                return new PlaceholderContext(guild, user, member, channel);
            }
        }
    }
}
