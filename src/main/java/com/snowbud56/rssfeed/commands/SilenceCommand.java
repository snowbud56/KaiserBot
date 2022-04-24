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

        if (event.getOption("silenceid") == null || event.getOption("seconds") == null) {
            FeedManager.sendFeedListMessage(event, commandName);
            return;
        }
        Feed feed = FeedManager.getFeed((int) event.getOption("silenceid").getAsLong());
        if (feed == null)
            event.reply("That feed doesn't exist!").queue();
        else {
            feed.setNotifying(false);
            feed.setTimeToNotify(System.currentTimeMillis() + (((int) event.getOption("seconds").getAsLong()) * 60000L));
            event.reply("Okay! I will stop notifying you for " + TimeUtil.getDuration(TimeUnit.FIT, (((int) event.getOption("seconds").getAsLong()) * 60000))).queue();
        }
    }

    @Override
    public CommandData getCommandData() {
        return commandData.addOptions(
                new OptionData(OptionType.INTEGER, "silenceid", "ID of the feed (use /info for feed IDs)", true),
                new OptionData(OptionType.INTEGER, "seconds", "Length in seconds", true)
                );
    }

    @Override
    protected String getDescription() {
        return "Silences a given feed for the given time.";
    }
}
