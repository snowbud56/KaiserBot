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
        if (event.getOption("checkid") == null) {
            FeedManager.sendFeedListMessage(event, commandName);
            return;
        }

        Feed feed = FeedManager.getFeed((int) event.getOption("checkid").getAsLong());
        if (feed == null)
            BotUtil.sendTemporaryMessage(channel, "That feed doesn't exist!", 10);
        else {
            if (!(feed instanceof LiveIncidentFeed))
                event.reply("Forcing a check for that feed...").queue();

            if (!feed.isEnabled()) {
                event.reply("That feed is disabled. I'm unable to force an update on a disabled feed. Please re-enable the feed in order to force an update.").queue();
            } else {
                feed.forceCheck();
            }
        }
    }

    @Override
    public void onButtonPress(Feed feed, ButtonClickEvent event) {
        feed.forceCheck();
        event.reply("Forced a check for " + feed.getName() + "!").queue();
    }

    @Override
    public CommandData getCommandData() {
        return commandData.addOption(OptionType.INTEGER, "checkid", "ID of the feed.");
    }

    @Override
    protected String getDescription() {
        return "Forces the given feed to update.";
    }
}
