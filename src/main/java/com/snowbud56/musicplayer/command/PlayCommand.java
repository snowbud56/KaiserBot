package com.snowbud56.musicplayer.command;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.snowbud56.command.CommandBase;
import com.snowbud56.musicplayer.GuildMusicManager;
import com.snowbud56.musicplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings("ConstantConditions")
public class PlayCommand extends CommandBase {

    public PlayCommand() {
        super("play");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        Member selfMember = guild.getSelfMember();
        GuildVoiceState selfVoiceState = selfMember.getVoiceState();
        if (!selfVoiceState.inVoiceChannel()) {
            reply(event, true, "I cannot play music when I am not in a voice channel.");
            return;
        }

        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();
        if (!memberVoiceState.inVoiceChannel()) {
            reply(event, true, "You need to be in a voice channel to control the music.");
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            reply(event, true, "I need to be in the same voice channel as you in order to control the music.");
            return;
        }

        if (event.getOption("search") == null) {
            GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);

            AudioPlayer audioPlayer = musicManager.audioPlayer;
            if (audioPlayer.getPlayingTrack() == null && musicManager.scheduler.queue.isEmpty()) {
                reply(event, true, "There is no music playing currently.");
                return;
            }

            if (!audioPlayer.isPaused()) {
                reply(event, true, "There is already a song playing.");
                return;
            }

            audioPlayer.setPaused(false);
            reply(event, true, "The track has been resumed.");
            return;
        }

        String link = event.getOption("search").getAsString();

        if (!isURL(link)) {
            link = "ytsearch:" + link;
        }

        defer(event, false);

        PlayerManager.getInstance().loadAndPlay(event, link);

    }

    private boolean isURL(String link) {
        try {
            new URI(link);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    @Override
    public CommandData getCommandData() {
        return commandData.addOption(OptionType.STRING, "search", "URL or keyword to search.");
    }

    @Override
    protected String getDescription() {
        return "Plays the given song. (YouTube URL and search words allowed)";
    }
}
