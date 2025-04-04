package com.noahhendrickson.warcoat.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DiscordBotConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordBotConfig.class);

    @Value("${discord.bot.token}")
    private String token;

    @Value("${discord.bot.activity}")
    private String activity;

    @Bean
    public ActivityParser activityParser() {
        return new ActivityParser();
    }

    @Bean
    public JDA jda() throws InterruptedException {
        LOGGER.info("Initializing Discord bot");

        JDA jda = JDABuilder.createDefault(token)
                .setActivity(activityParser().parse(activity))
                .enableIntents(
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.GUILD_MESSAGE_REACTIONS,
                        GatewayIntent.MESSAGE_CONTENT
                )
                .enableCache(
                        CacheFlag.MEMBER_OVERRIDES,
                        CacheFlag.ROLE_TAGS,
                        CacheFlag.EMOJI,
                        CacheFlag.VOICE_STATE
                )
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .build();

        jda.awaitReady();
        LOGGER.info("Discord bot initialized and connected successfully");

        return jda;
    }
}
