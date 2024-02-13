package com.snowbud56.musicplayer.command;

import com.snowbud56.command.CommandBase;
import com.snowbud56.musicplayer.GuildMusicManager;
import com.snowbud56.musicplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class VolumeCommand extends CommandBase {

    public VolumeCommand() {
        super("volume");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        Member selfMember = guild.getSelfMember();
        GuildVoiceState selfVoiceState = selfMember.getVoiceState();
        if (!selfVoiceState.inVoiceChannel()) {
            reply(event,true, "I cannot play music when I am not in a voice channel.");
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

        Integer volume = (int) event.getOption("volume").getAsLong();
        if (volume < 0 && volume > 100) {
            reply(event, true, "The volume must be between 0 and 100!");
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);

        musicManager.audioPlayer.setVolume(volume);
        reply(event, true, "The volume has been set to `" + volume + "`!");
    }

    @Override
    public CommandData getCommandData() {
        return commandData.addOption(OptionType.INTEGER, "volume", "Volume between 1 and 100.", true);
    }

    @Override
    protected String getDescription() {
        return "Sets the volume for the music.";
    }
}
