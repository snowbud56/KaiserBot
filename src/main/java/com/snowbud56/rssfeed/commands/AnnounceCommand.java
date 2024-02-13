package com.snowbud56.rssfeed.commands;

import com.snowbud56.command.CommandBase;
import com.snowbud56.rssfeed.feeds.BlueRidgeFeed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.IOException;

public class AnnounceCommand extends CommandBase {
    public AnnounceCommand() {
        super("announce");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        defer(event, true);

        if (event.getOption("band").getAsString().equalsIgnoreCase("all")) {
            BlueRidgeFeed.reannounceAllBands();

            return;
        }
        if (BlueRidgeFeed.refresh(channel.getJDA(), false, event.getOption("band").getAsString())) {
            editReply(event, ":+1:");
        } else {
            editReply(event, "This is currently refreshing, please try again in a minute.");
        }
    }

    @Override
    public CommandData getCommandData() {
        return commandData.addOptions(
                new OptionData(OptionType.STRING, "band", "Name of the band to (re)announce.", true)
        );
    }

    @Override
    protected String getDescription() {
        return "Forces the bot to re-announce a band that may have previously been skipped.";
    }
}
