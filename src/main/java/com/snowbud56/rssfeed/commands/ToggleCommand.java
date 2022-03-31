package com.snowbud56.rssfeed.commands;

import com.snowbud56.command.CommandBase;
import com.snowbud56.rssfeed.FeedManager;
import com.snowbud56.rssfeed.feeds.Feed;
import com.snowbud56.utils.BotUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

public class ToggleCommand extends CommandBase {

    public ToggleCommand() {
        super("toggle", "enable", "disable");
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
                feed.setEnabled(!feed.isEnabled());
            }
        } catch (IllegalArgumentException e) {
            BotUtil.sendTemporaryMessage(channel, "Invalid integer, please provide a valid number for that feed.", 10);
        }
    }
}
