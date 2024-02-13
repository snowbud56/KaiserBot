package com.snowbud56.musicplayer.command;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.snowbud56.command.CommandBase;
import com.snowbud56.musicplayer.GuildMusicManager;
import com.snowbud56.musicplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class SkipCommand extends CommandBase {

    public SkipCommand() {
        super("skip");
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

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);

        AudioPlayer audioPlayer = musicManager.audioPlayer;
        if (audioPlayer.getPlayingTrack() == null) {
            reply(event, true, "There is no music playing currently.");
            return;
        }

        musicManager.scheduler.nextTrack();
        reply(event, false, "The track has been skipped.");
    }

    @Override
    public CommandData getCommandData() {
        return commandData;
    }

    @Override
    protected String getDescription() {
        return "Skips the current song and moves to the next song.";
    }
}
