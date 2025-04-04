package com.noahhendrickson.warcoat.config;

import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parser for Discord bot activity status.
 * Format: TYPE:message
 * Examples:
 * - PLAYING:Minecraft
 * - WATCHING:YouTube
 * - LISTENING:Spotify
 * - COMPETING:tournaments
 * - STREAMING:https://twitch.tv/username:Speedruns
 */
public class ActivityParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActivityParser.class);

    /**
     * Parse activity string into JDA Activity object.
     *
     * @param activityStr Format: TYPE:message
     * @return Activity object or default PLAYING activity if parsing fails
     */
    public Activity parse(String activityStr) {
        if (activityStr == null || activityStr.isEmpty()) {
            LOGGER.warn("Activity string is null or empty");
            return null;
        }

        try {
            String[] parts = activityStr.split(":", 3);

            ActivityType type = parseActivityType(parts[0]);

            if (type == ActivityType.STREAMING && parts.length == 3) {
                // STREAMING format: STREAMING:url:message
                return Activity.streaming(parts[2], parts[1]);
            } else if (parts.length >= 2) {
                return createActivity(type, parts[1]);
            } else {
                LOGGER.warn("Invalid activity format: {}, expected TYPE:message, defaulting to 'CUSTOM'", activityStr);
                return Activity.customStatus(activityStr);
            }
        } catch (Exception e) {
            LOGGER.warn("Failed to parse activity string: {}, defaulting to 'CUSTOM'", activityStr, e);
            return Activity.customStatus(activityStr);
        }
    }

    private ActivityType parseActivityType(String typeStr) {
        try {
            return ActivityType.valueOf(typeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Invalid activity type: {}, defaulting to 'CUSTOM'", typeStr);
            return ActivityType.CUSTOM;
        }
    }

    private Activity createActivity(ActivityType type, String message) {
        return switch (type) {
            case PLAYING -> Activity.playing(message);
            case WATCHING -> Activity.watching(message);
            case LISTENING -> Activity.listening(message);
            case COMPETING -> Activity.competing(message);
            case STREAMING -> Activity.streaming(message, null);
            case CUSTOM -> Activity.customStatus(message);
        };
    }

    public enum ActivityType {
        PLAYING,
        WATCHING,
        LISTENING,
        COMPETING,
        STREAMING,
        CUSTOM
    }
}
