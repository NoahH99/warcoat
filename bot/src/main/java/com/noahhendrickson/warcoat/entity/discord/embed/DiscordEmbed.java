package com.noahhendrickson.warcoat.entity.discord.embed;

import com.noahhendrickson.common.util.PlaceholderProcessor;
import com.noahhendrickson.warcoat.entity.JdaConvertible;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiscordEmbed implements JdaConvertible<MessageEmbed> {

    private String title;
    private String description;
    private String url;
    private Integer color;
    private LocalDateTime timestamp;
    private EmbedFooter footer;
    private EmbedImage thumbnail;
    private EmbedImage image;
    private EmbedAuthor author;
    private List<EmbedField> fields;

    /**
     * Create a DiscordEmbed from a JDA MessageEmbed
     *
     * @param messageEmbed The JDA MessageEmbed to convert
     * @return A new DiscordEmbed instance
     */
    public static DiscordEmbed fromJdaEmbed(MessageEmbed messageEmbed) {
        if (messageEmbed == null) return null;

        // Convert fields
        List<EmbedField> embedFields = null;

        if (!messageEmbed.getFields().isEmpty()) {
            embedFields = new ArrayList<>();

            for (MessageEmbed.Field field : messageEmbed.getFields()) {
                embedFields.add(EmbedField.builder()
                        .name(field.getName())
                        .value(field.getValue())
                        .inline(field.isInline())
                        .build());
            }
        }

        // Convert footer
        EmbedFooter footer = null;
        if (messageEmbed.getFooter() != null) {
            footer = EmbedFooter.builder()
                    .text(messageEmbed.getFooter().getText())
                    .iconUrl(messageEmbed.getFooter().getIconUrl())
                    .build();
        }

        // Convert thumbnail
        EmbedImage thumbnail = null;
        if (messageEmbed.getThumbnail() != null) {
            thumbnail = EmbedImage.builder()
                    .url(messageEmbed.getThumbnail().getUrl())
                    .height(messageEmbed.getThumbnail().getHeight())
                    .width(messageEmbed.getThumbnail().getWidth())
                    .build();
        }

        // Convert image
        EmbedImage image = null;
        if (messageEmbed.getImage() != null) {
            image = EmbedImage.builder()
                    .url(messageEmbed.getImage().getUrl())
                    .height(messageEmbed.getImage().getHeight())
                    .width(messageEmbed.getImage().getWidth())
                    .build();
        }

        // Convert author
        EmbedAuthor author = null;
        if (messageEmbed.getAuthor() != null) {
            author = EmbedAuthor.builder()
                    .name(messageEmbed.getAuthor().getName())
                    .url(messageEmbed.getAuthor().getUrl())
                    .iconUrl(messageEmbed.getAuthor().getIconUrl())
                    .build();
        }

        // Convert timestamp
        LocalDateTime timestamp = null;
        if (messageEmbed.getTimestamp() != null) timestamp = messageEmbed.getTimestamp().toLocalDateTime();

        // Convert color
        Integer color = null;
        if (messageEmbed.getColor() != null) color = messageEmbed.getColor().getRGB();

        return DiscordEmbed.builder()
                .title(messageEmbed.getTitle())
                .description(messageEmbed.getDescription())
                .url(messageEmbed.getUrl())
                .color(color)
                .timestamp(timestamp)
                .footer(footer)
                .thumbnail(thumbnail)
                .image(image)
                .author(author)
                .fields(embedFields)
                .build();
    }

    @Override
    public MessageEmbed toJdaEntity() {
        return buildEmbed(null, null);
    }

    @Override
    public MessageEmbed toJdaEntity(Guild guild, User user) {
        return buildEmbed(guild, user);
    }

    private MessageEmbed buildEmbed(Guild guild, User user) {
        EmbedBuilder builder = new EmbedBuilder();
        boolean hasContext = guild != null && user != null;

        if (title != null && !title.isBlank()) {
            String processedTitle = hasContext ? PlaceholderProcessor.process(title, guild, user) : title;
            if (!processedTitle.isBlank()) builder.setTitle(processedTitle);
        }

        if (description != null && !description.isBlank()) {
            String processedDesc = hasContext ? PlaceholderProcessor.process(description, guild, user) : description;
            if (!processedDesc.isBlank()) builder.setDescription(processedDesc);
        }

        if (url != null && !url.isBlank()) builder.setUrl(url);
        if (color != null) builder.setColor(new Color(color));


        if (timestamp != null) {
            Instant instant = timestamp.toInstant(ZoneOffset.UTC);
            builder.setTimestamp(instant);
        }

        // Footer
        if (footer != null && footer.getText() != null && !footer.getText().isBlank()) {
            String text = hasContext ? PlaceholderProcessor.process(footer.getText(), guild, user) : footer.getText();

            if (!text.isBlank()) {
                String iconUrl = footer.getIconUrl();

                if (iconUrl != null && !iconUrl.isBlank()) builder.setFooter(text, iconUrl);
                else builder.setFooter(text);
            }
        }

        // Thumbnail
        if (thumbnail != null && thumbnail.getUrl() != null && !thumbnail.getUrl().isBlank()) {
            String url = hasContext ? PlaceholderProcessor.process(thumbnail.getUrl(), guild, user) : thumbnail.getUrl();

            if (!url.isBlank()) builder.setThumbnail(url);
        }

        // Image
        if (image != null && image.getUrl() != null && !image.getUrl().isBlank()) {
            String url = hasContext ? PlaceholderProcessor.process(image.getUrl(), guild, user) : image.getUrl();

            if (!url.isBlank()) builder.setImage(url);
        }

        // Author
        if (author != null && author.getName() != null && !author.getName().isBlank()) {
            String name = hasContext ? PlaceholderProcessor.process(author.getName(), guild, user) : author.getName();
            if (!name.isBlank()) {
                String url = author.getUrl();
                String iconUrl = author.getIconUrl();

                boolean validUrl = url != null && !url.isBlank();
                boolean validIconUrl = iconUrl != null && !iconUrl.isBlank();

                if (validUrl && validIconUrl) {
                    builder.setAuthor(name, url, iconUrl);
                } else if (validUrl) {
                    builder.setAuthor(name, url);
                } else if (validIconUrl) {
                    builder.setAuthor(name, null, iconUrl);
                } else {
                    builder.setAuthor(name);
                }
            }
        }

        // Fields
        if (fields != null) {
            for (EmbedField field : fields) {
                if (field.getName() != null && !field.getName().isBlank() &&
                        field.getValue() != null && !field.getValue().isBlank()) {

                    String name = hasContext ? PlaceholderProcessor.process(field.getName(), guild, user) : field.getName();
                    String value = hasContext ? PlaceholderProcessor.process(field.getValue(), guild, user) : field.getValue();

                    if (!name.isBlank() && !value.isBlank()) {
                        boolean inline = field.getInline() != null && field.getInline();
                        builder.addField(name, value, inline);
                    }
                }
            }
        }

        return builder.build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmbedFooter {
        private String text;
        private String iconUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmbedImage {
        private String url;
        private Integer height;
        private Integer width;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmbedAuthor {
        private String name;
        private String url;
        private String iconUrl;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmbedField {
        private String name;
        private String value;
        private Boolean inline;
    }
}