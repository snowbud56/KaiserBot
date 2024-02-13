package com.snowbud56.musicplayer.command;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.snowbud56.command.CommandBase;
import com.snowbud56.musicplayer.GuildMusicManager;
import com.snowbud56.musicplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class PauseCommand extends CommandBase {

    public PauseCommand() {
        super("pause");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        Member selfMember = guild.getSelfMember();
        GuildVoiceState selfVoiceState = selfMember.getVoiceState();
        if (!selfVoiceState.inVoiceChannel()) {
            reply(event, true, "I cannot play music when I am not in a voice channel.");
            return;
        }


        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);

        AudioPlayer audioPlayer = musicManager.audioPlayer;
        if (audioPlayer.getPlayingTrack() == null || audioPlayer.isPaused()) {
            reply(event, true,  "There is no music playing currently.");
            return;
        }

        audioPlayer.setPaused(true);
        reply(event,true,  "The track has been paused.");
    }

    @Override
    public CommandData getCommandData() {
        return commandData;
    }

    @Override
    protected String getDescription() {
        return "Pauses the currently playing song.";
    }
}
