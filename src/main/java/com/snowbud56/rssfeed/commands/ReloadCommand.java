package com.snowbud56.rssfeed.commands;

/*
 * Created by snowbud56 on September 17, 2020
 * Do not change or use this code without permission
 */

import com.snowbud56.command.CommandBase;
import com.snowbud56.config.Config;
import com.snowbud56.rssfeed.FeedManager;
import com.snowbud56.rssfeed.feeds.FeedInitializer;
import com.snowbud56.utils.BotUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

public class ReloadCommand extends CommandBase {

    public ReloadCommand() {
        super("reload", "rl");
    }

    @Override
    public void execute(Member member, MessageChannel channel, String[] args) {
        if (member.getUser().getId().equals(Config.getInstance().getString("ownerID"))) {
            FeedManager.clearFeeds();
            Config.reloadConfig();
            FeedInitializer.initializeFeeds();
            BotUtil.sendTemporaryMessage(channel, "Reloaded feeds :+1:", 10);
        } else {
            BotUtil.sendTemporaryMessage(channel, "You don't have permission to execute this command, only the owner can execute this command.", 10);
        }
    }
}
