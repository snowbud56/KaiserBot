package com.snowbud56.rssfeed.commands;

/*
 * Created by snowbud56 on June 11, 2020
 * Do not change or use this code without permission
 */

import com.snowbud56.command.CommandBase;
import com.snowbud56.rssfeed.FeedManager;
import com.snowbud56.rssfeed.feeds.Feed;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.concurrent.TimeUnit;

public class UpdateCommand extends CommandBase {

    public UpdateCommand() {
        super("check");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        FeedManager.sendFeedListMessage(event, commandName);
    }

    @Override
    public void onButtonPress(Feed feed, ButtonClickEvent event) {
        event.reply("Forcing a check for " + feed.getName() + "!").setEphemeral(true).queue();
        feed.forceCheck();
    }

    @Override
    public CommandData getCommandData() {
        return commandData;
    }

    @Override
    protected String getDescription() {
        return "Forces the selected feed to update.";
    }
}
