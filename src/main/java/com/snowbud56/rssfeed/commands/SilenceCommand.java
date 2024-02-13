package com.snowbud56.rssfeed.commands;

import com.snowbud56.command.CommandBase;
import com.snowbud56.rssfeed.FeedManager;
import com.snowbud56.rssfeed.feeds.Feed;
import com.snowbud56.utils.BotUtil;
import com.snowbud56.utils.TimeUnit;
import com.snowbud56.utils.TimeUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.GenericInteractionCreateEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SilenceCommand extends CommandBase {

    public SilenceCommand() {
        super("silence");
    }

    @Override
    public void execute(SlashCommandEvent event) {

        if (event.getOption("id") == null || event.getOption("minutes") == null) {
            FeedManager.sendFeedListMessage(event, commandName);
            return;
        }
        Feed feed = FeedManager.getFeed((int) event.getOption("id").getAsLong());
        if (feed == null)
            reply(event, true, "That feed doesn't exist!");
        else {
            feed.setNotifying(false);
            feed.setTimeToNotify(System.currentTimeMillis() + (((int) event.getOption("minutes").getAsLong()) * 60000L));
            reply(event, true, "Okay! I will stop notifying you for " + TimeUtil.getDuration(TimeUnit.FIT, ((event.getOption("minutes").getAsLong()) * 60000)) + " for the " + feed.getName());
        }
    }

    @Override
    public CommandData getCommandData() {
        return commandData.addOptions(
                new OptionData(OptionType.INTEGER, "id", "ID of the feed (use /info for feed IDs)", true),
                new OptionData(OptionType.INTEGER, "minutes", "Length in minutes", true)
                );
    }

    @Override
    protected String getDescription() {
        return "Silences the given feed for the given time.";
    }
}
