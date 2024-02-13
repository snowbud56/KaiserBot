package com.snowbud56.musicplayer.command;

import com.snowbud56.command.CommandBase;
import com.snowbud56.musicplayer.GuildMusicManager;
import com.snowbud56.musicplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class StopCommand extends CommandBase {

    public StopCommand() {
        super("stop");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        Member selfMember = guild.getSelfMember();
        GuildVoiceState selfVoiceState = selfMember.getVoiceState();
        if (!selfVoiceState.inVoiceChannel()) {
            reply(event, true, "I cannot control music playback when I am not in a voice channel.");
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);

        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();

        reply(event, true, "Playback has been stopped and the queue has been cleared.");

    }

    @Override
    public CommandData getCommandData() {
        return commandData;
    }

    @Override
    protected String getDescription() {
        return "Stops playing music and clears the queue.";
    }
}
