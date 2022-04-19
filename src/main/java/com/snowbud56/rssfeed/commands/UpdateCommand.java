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
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

public class UpdateCommand extends CommandBase {

    public UpdateCommand() {
        super("check", "update");
    }

    @Override
    public void execute(Member member, MessageChannel channel, String[] args) {
        if (args.length == 0) {
            FeedManager.sendFeedListMessage(channel);
            return;
        }
        try {
            Feed feed = FeedManager.getFeed(Integer.parseInt(args[0]));
            if (feed == null)
                BotUtil.sendTemporaryMessage(channel, "That feed doesn't exist!", 10);
            else {
                if (!(feed instanceof LiveIncidentFeed))
                    BotUtil.sendTemporaryMessage(channel, "Forcing a check for that feed...", 10);


                if (!feed.isEnabled()) {
                    BotUtil.sendTemporaryMessage(channel, "That feed is disabled. I'm unable to force an update on a disabled feed. Please re-enable the feed in order to force an update.", 10);
                } else {
                    //set nextCheck to currentMillis - checkCooldown
                    //access the feed's thread and run it forcefully
                    feed.forceCheck();
                }
            }
        } catch (IllegalArgumentException e) {
            BotUtil.sendTemporaryMessage(channel, "Invalid integer, please provide a valid number for that feed.", 10);
        }
    }
}
