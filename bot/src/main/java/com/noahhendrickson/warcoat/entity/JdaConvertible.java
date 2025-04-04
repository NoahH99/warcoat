package com.noahhendrickson.warcoat.entity;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;

/**
 * Interface for entities that can be converted to JDA entities.
 * @param <T> The JDA entity type
 */
public interface JdaConvertible<T> {
    
    /**
     * Convert this entity to its JDA counterpart.
     * 
     * @return The JDA entity
     */
    T toJdaEntity();
    
    /**
     * Convert this entity to its JDA counterpart, with placeholder processing.
     * 
     * @param guild The guild context for placeholder processing
     * @param user The user context for placeholder processing
     * @return The JDA entity with placeholders processed
     */
    T toJdaEntity(Guild guild, User user);
}