package com.snowbud56.rssfeed.commands;

import com.snowbud56.command.CommandBase;
import com.snowbud56.rssfeed.FeedManager;
import com.snowbud56.rssfeed.feeds.Feed;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.concurrent.TimeUnit;

public class ToggleCommand extends CommandBase {

    public ToggleCommand() {
        super("toggle");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        FeedManager.sendFeedListMessage(event, commandName);
    }

    @Override
    public void onButtonPress(Feed feed, ButtonClickEvent event) {
        feed.setEnabled(!feed.isEnabled());
        event.reply(feed.getName() + " is now " + (feed.isEnabled() ? "enabled" : "disabled") + "!").setEphemeral(true).queue((message) -> message.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
    }

    @Override
    public CommandData getCommandData() {
        return commandData;
    }

    @Override
    protected String getDescription() {
        return "Toggles the selected feed.";
    }
}
