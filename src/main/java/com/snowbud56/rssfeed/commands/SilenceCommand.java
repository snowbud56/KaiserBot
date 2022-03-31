package com.snowbud56.rssfeed.commands;

import com.snowbud56.command.CommandBase;
import com.snowbud56.rssfeed.FeedManager;
import com.snowbud56.rssfeed.feeds.Feed;
import com.snowbud56.utils.BotUtil;
import com.snowbud56.utils.TimeUnit;
import com.snowbud56.utils.TimeUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

public class SilenceCommand extends CommandBase {

    public SilenceCommand() {
        super("silence", "donotdisturb", "dnd", "shutup");
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
                feed.setNotifying(false);
                feed.setTimeToNotify(System.currentTimeMillis() + (Integer.parseInt((args[1])) * 60000L));
                BotUtil.sendTemporaryMessage(channel, "Okay! I will stop notifying you for " + TimeUtil.getDuration(TimeUnit.FIT, (Integer.parseInt((args[1])) * 60000)), 10);
            }
        } catch (IllegalArgumentException e) {
            BotUtil.sendMessage(channel, "Invalid argument! Please put **2** numbers. Usage: /silence <Feed ID> <Amount (in minutes)>");
        }
    }
}
