package com.noahhendrickson.warcoat.repository.welcome;

import com.noahhendrickson.warcoat.entity.welcome.MessageType;
import com.noahhendrickson.warcoat.entity.welcome.WelcomeMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WelcomeMessageRepository extends JpaRepository<WelcomeMessage, Long> {

    Optional<WelcomeMessage> findByEnabledAndGuildIdAndMessageType(Boolean enabled, Long guildId, MessageType messageType);
}
