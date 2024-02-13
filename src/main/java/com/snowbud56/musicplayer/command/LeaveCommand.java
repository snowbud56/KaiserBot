package com.snowbud56.musicplayer.command;

import com.snowbud56.command.CommandBase;
import com.snowbud56.musicplayer.GuildMusicManager;
import com.snowbud56.musicplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.managers.AudioManager;

public class LeaveCommand extends CommandBase {

    public LeaveCommand() {
        super("leave");
    }

    @Override
    public void execute(SlashCommandEvent event) {

        Member selfMember = guild.getSelfMember();
        GuildVoiceState selfVoiceState = selfMember.getVoiceState();
        if (!selfVoiceState.inVoiceChannel()) {
            reply(event, true, "I'm not currently in a voice channel.");
            return;
        }

        guild.getAudioManager().closeAudioConnection();

        reply(event, true, "I have left the voice channel.");
    }

    @Override
    public CommandData getCommandData() {
        return commandData;
    }

    @Override
    protected String getDescription() {
        return "Makes the bot leave the voice channel.";
    }
}
