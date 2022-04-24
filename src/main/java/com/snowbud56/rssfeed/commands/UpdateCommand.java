package com.snowbud56.rssfeed.commands;

/*
 * Created by snowbud56 on June 11, 2020
 * Do not change or use this code without permission
 */

import com.snowbud56.command.CommandBase;
import com.snowbud56.rssfeed.FeedManager;
import com.snowbud56.rssfeed.feeds.Feed;
import com.snowbud56.rssfeed.feeds.LiveIncidentFeed;
import com.snowbud56.utils.BotUtil;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

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
        event.reply("Forcing a check for " + feed.getName() + "!").queue();
        feed.forceCheck();
    }

    @Override
    public CommandData getCommandData() {
        return commandData;
    }

    @Override
    protected String getDescription() {
        return "Forces the given feed to update.";
    }
}
