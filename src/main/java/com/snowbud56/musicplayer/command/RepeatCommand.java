package com.snowbud56.musicplayer.command;

import com.snowbud56.command.CommandBase;
import com.snowbud56.musicplayer.GuildMusicManager;
import com.snowbud56.musicplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class RepeatCommand extends CommandBase {

    public RepeatCommand() {
        super("repeat");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);

        musicManager.scheduler.repeating = !musicManager.scheduler.repeating;
        reply(event, true, "This song is now " + (musicManager.scheduler.repeating ? "" : "not ") + "repeating.");

    }

    @Override
    public CommandData getCommandData() {
        return commandData;
    }

    @Override
    protected String getDescription() {
        return "Toggles whether or not the song will repeat or go to the next song.";
    }
}
