package com.snowbud56.musicplayer.command;

import com.snowbud56.command.CommandBase;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.EnumSet;

@SuppressWarnings("ConstantConditions")
public class JoinCommand extends CommandBase {

    public JoinCommand() {
        super("join");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        MessageChannel channel = event.getChannel();
        Member selfMember = guild.getSelfMember();
        GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        if (selfVoiceState.inVoiceChannel()) {
            reply(event, true, "I'm already in a voice channel.");
            return;
        }

        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();
        if (!memberVoiceState.inVoiceChannel()) {
            reply(event,true,  "You need to be in a voice channel for this command to work.");
            return;
        }

        AudioManager audioManager = guild.getAudioManager();
        VoiceChannel voiceChannel = memberVoiceState.getChannel();

        if (!selfMember.hasPermission(voiceChannel, EnumSet.of(Permission.VOICE_CONNECT, Permission.VOICE_SPEAK))) {
            event.reply("I do not have permission to join or speak in the channel that you're in.").queue();
        }

        audioManager.openAudioConnection(voiceChannel);
        reply(event, true, "Connecting to \uD83D\uDD0A " + voiceChannel.getName());

    }

    @Override
    public CommandData getCommandData() {
        return commandData;
    }

    @Override
    protected String getDescription() {
        return "Makes the bot join the voice channel that you're in.";
    }
}
