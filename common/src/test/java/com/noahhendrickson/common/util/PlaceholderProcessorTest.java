package com.noahhendrickson.common.util;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.*;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

class PlaceholderProcessorTest {

    @Mock
    private Guild guild;

    @Mock
    private User user;

    @Mock
    private Member member;

    @Mock
    private TextChannel channel;

    @Mock
    private Role role1;

    @Mock
    private Role role2;

    private PlaceholderProcessor.PlaceholderContext context;
    private OffsetDateTime testTime;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testTime = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);

        // Setup guild mocks
        when(guild.getName()).thenReturn("Test Guild");
        when(guild.getId()).thenReturn("123456789");
        when(guild.getIconUrl()).thenReturn("https://example.com/icon.png");
        when(guild.getMemberCount()).thenReturn(100);
        when(guild.getOwnerId()).thenReturn("987654321");
        when(guild.getBoostCount()).thenReturn(5);
        when(guild.getBoostTier()).thenReturn(Guild.BoostTier.TIER_2);
        when(guild.getMember(any(UserSnowflake.class))).thenReturn(member);

        // Setup user mocks
        when(user.getName()).thenReturn("TestUser");
        when(user.getId()).thenReturn("111222333");
        when(user.getAsMention()).thenReturn("<@111222333>");
        when(user.getDiscriminator()).thenReturn("1234");
        when(user.getGlobalName()).thenReturn("Global Test User");
        when(user.getEffectiveAvatarUrl()).thenReturn("https://example.com/avatar.png");
        when(user.getTimeCreated()).thenReturn(testTime);
        when(user.isBot()).thenReturn(false);

        // Setup member mocks
        when(member.getEffectiveName()).thenReturn("Member Nickname");
        when(member.getNickname()).thenReturn("Nick");
        when(member.getUser()).thenReturn(user);
        when(member.getColorRaw()).thenReturn(Color.RED.getRGB());
        when(member.getColor()).thenReturn(Color.RED);
        when(member.getTimeJoined()).thenReturn(testTime);
        when(member.getTimeBoosted()).thenReturn(testTime);

        List<Role> roles = new ArrayList<>();
        when(role1.getName()).thenReturn("Admin");
        when(role2.getName()).thenReturn("Moderator");
        roles.add(role1);
        roles.add(role2);
        when(member.getRoles()).thenReturn(roles);

        // Setup channel mocks
        when(channel.getName()).thenReturn("test-channel");
        when(channel.getId()).thenReturn("444555666");
        when(channel.getAsMention()).thenReturn("<#444555666>");
        when(channel.getTopic()).thenReturn("Channel Topic");
        when(channel.getTimeCreated()).thenReturn(testTime);
        when(channel.getPosition()).thenReturn(3);
        when(channel.isNSFW()).thenReturn(false);

        // Setup owner
        Member ownerMember = mock(Member.class);
        when(ownerMember.getUser()).thenReturn(user);
        when(guild.getOwner()).thenReturn(ownerMember);

        context = PlaceholderProcessor.PlaceholderContext.builder()
                .guild(guild)
                .user(user)
                .member(member)
                .channel(channel)
                .build();
    }

    @Test
    void testNullTemplate() {
        assertNull(PlaceholderProcessor.process(null, context));
    }

    @Test
    void testEmptyTemplate() {
        assertEquals("", PlaceholderProcessor.process("", context));
    }

    @Test
    void testTemplateWithNoPlaceholders() {
        assertEquals("Hello, world!", PlaceholderProcessor.process("Hello, world!", context));
    }

    @Test
    void testServerPlaceholders() {
        assertEquals("Test Guild", PlaceholderProcessor.process("{server}", context));
        assertEquals("Test Guild", PlaceholderProcessor.process("{server.name}", context));
        assertEquals("123456789", PlaceholderProcessor.process("{server.id}", context));
        assertEquals("https://example.com/icon.png", PlaceholderProcessor.process("{server.icon_url}", context));
        assertEquals("100", PlaceholderProcessor.process("{server.member_count}", context));
        assertEquals("TestUser", PlaceholderProcessor.process("{server.owner}", context));
        assertEquals("987654321", PlaceholderProcessor.process("{server.owner_id}", context));
        assertEquals("5", PlaceholderProcessor.process("{server.boost_count}", context));
        assertEquals("TIER_2", PlaceholderProcessor.process("{server.boost_tier}", context));
    }

    @Test
    void testUserPlaceholders() {
        assertEquals("TestUser", PlaceholderProcessor.process("{user}", context));
        assertEquals("TestUser", PlaceholderProcessor.process("{user.name}", context));
        assertEquals("111222333", PlaceholderProcessor.process("{user.id}", context));
        assertEquals("<@111222333>", PlaceholderProcessor.process("{user.mention}", context));
        assertEquals("1234", PlaceholderProcessor.process("{user.discriminator}", context));
        assertEquals("Global Test User", PlaceholderProcessor.process("{user.global_name}", context));
        assertEquals("https://example.com/avatar.png", PlaceholderProcessor.process("{user.avatar_url}", context));
        assertEquals(testTime.toString(), PlaceholderProcessor.process("{user.created_time}", context));
        assertEquals("false", PlaceholderProcessor.process("{user.is_bot}", context));
    }

    @Test
    void testMemberPlaceholders() {
        assertEquals("Member Nickname", PlaceholderProcessor.process("{member}", context));
        assertEquals("Nick", PlaceholderProcessor.process("{member.nickname}", context));
        assertEquals("Member Nickname", PlaceholderProcessor.process("{member.effective_name}", context));
        assertEquals("#" + Integer.toHexString(Color.RED.getRGB()).toUpperCase(),
                PlaceholderProcessor.process("{member.color}", context));
        assertEquals(String.valueOf(Color.RED.getRGB()), PlaceholderProcessor.process("{member.color_raw}", context));
        assertEquals(testTime.toString(), PlaceholderProcessor.process("{member.joined_time}", context));
        assertEquals(testTime.toString(), PlaceholderProcessor.process("{member.boosting_since}", context));
        assertEquals("Admin, Moderator", PlaceholderProcessor.process("{member.roles}", context));
        assertEquals("Admin", PlaceholderProcessor.process("{member.highest_role}", context));
    }

    @Test
    void testChannelPlaceholders() {
        assertEquals("test-channel", PlaceholderProcessor.process("{channel}", context));
        assertEquals("test-channel", PlaceholderProcessor.process("{channel.name}", context));
        assertEquals("444555666", PlaceholderProcessor.process("{channel.id}", context));
        assertEquals("<#444555666>", PlaceholderProcessor.process("{channel.mention}", context));
        assertEquals("Channel Topic", PlaceholderProcessor.process("{channel.topic}", context));
        assertEquals(testTime.toString(), PlaceholderProcessor.process("{channel.created_time}", context));
        assertEquals("3", PlaceholderProcessor.process("{channel.position}", context));
        assertEquals("false", PlaceholderProcessor.process("{channel.nsfw}", context));
    }

    @Test
    void testMultiplePlaceholders() {
        String template = "Welcome {user.name} to {server.name}! You can chat in {channel.mention}.";
        String expected = "Welcome TestUser to Test Guild! You can chat in <#444555666>.";
        assertEquals(expected, PlaceholderProcessor.process(template, context));
    }

    @Test
    void testInvalidPlaceholders() {
        assertEquals("{invalid}", PlaceholderProcessor.process("{invalid}", context));
        assertEquals("{server.invalid}", PlaceholderProcessor.process("{server.invalid}", context));
    }

    @Test
    void testNullContext() {
        PlaceholderProcessor.PlaceholderContext nullContext = PlaceholderProcessor.PlaceholderContext.builder().build();
        assertEquals("", PlaceholderProcessor.process("{server}", nullContext));
        assertEquals("", PlaceholderProcessor.process("{user}", nullContext));
        assertEquals("", PlaceholderProcessor.process("{member}", nullContext));
        assertEquals("", PlaceholderProcessor.process("{channel}", nullContext));
    }

    @Test
    void testConvenienceMethods() {
        // Test Guild + User convenience method
        String template = "Welcome {user.name} to {server.name}!";
        String expected = "Welcome TestUser to Test Guild!";
        assertEquals(expected, PlaceholderProcessor.process(template, guild, user));

        // Test Guild + Channel + User convenience method
        template = "Welcome {user.name} to {server.name} in {channel.name}!";
        expected = "Welcome TestUser to Test Guild in test-channel!";
        assertEquals(expected, PlaceholderProcessor.process(template, guild, channel, user));

        // Test with null values
        assertNull(PlaceholderProcessor.process(null, guild, user));
        assertNull(PlaceholderProcessor.process(null, guild, channel, user));
    }

    @Test
    void testMemberWithEmptyRoles() {
        when(member.getRoles()).thenReturn(Collections.emptyList());
        assertEquals("", PlaceholderProcessor.process("{member.roles}", context));
        assertEquals("", PlaceholderProcessor.process("{member.highest_role}", context));
    }

    @Test
    void testMemberWithNullColor() {
        when(member.getColor()).thenReturn(null);
        assertEquals("", PlaceholderProcessor.process("{member.color}", context));
    }

    @Test
    void testMemberWithNullBoostTime() {
        when(member.getTimeBoosted()).thenReturn(null);
        assertEquals("", PlaceholderProcessor.process("{member.boosting_since}", context));
    }

    @Test
    void testGuildWithNullOwner() {
        when(guild.getOwner()).thenReturn(null);
        assertEquals("", PlaceholderProcessor.process("{server.owner}", context));
    }
}