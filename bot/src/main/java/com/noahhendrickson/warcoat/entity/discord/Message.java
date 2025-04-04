package com.noahhendrickson.warcoat.entity.discord;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;
    
    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;
    
    @Column(columnDefinition = "TEXT")
    private String content;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(name = "edited_timestamp")
    private LocalDateTime editedTimestamp;
    
    @Column(nullable = false)
    private Boolean tts;
    
    @Column(name = "mention_everyone", nullable = false)
    private Boolean mentionEveryone;
    
    @Column(nullable = false)
    private Boolean pinned;
    
    @Column(nullable = false)
    private Short type;
    
    private Integer flags;
    
    @ManyToOne
    @JoinColumn(name = "reference_message_id")
    private Message referenceMessage;
}