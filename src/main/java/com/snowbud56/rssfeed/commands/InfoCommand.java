package com.snowbud56.rssfeed.commands;

/*
 * Created by snowbud56 on June 12, 2020
 * Do not change or use this code without permission
 */

import com.snowbud56.command.CommandBase;
import com.snowbud56.rssfeed.FeedManager;
import com.snowbud56.rssfeed.feeds.Feed;
import com.snowbud56.utils.BotUtil;
import com.snowbud56.utils.TimeUnit;
import com.snowbud56.utils.TimeUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class InfoCommand extends CommandBase {

    public InfoCommand() {
        super("info", "feeds");
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
                EmbedBuilder embed = new EmbedBuilder();
                embed.setColor(new Color(255, 254, 0));
                embed.setTitle(feed.getName());

                HashMap<String, Object> feedProperties = feed.getProperties();

                for (Map.Entry<String, Object> property : feedProperties.entrySet()) {
                    embed.addField(property.getKey(), property.getValue().toString(), false);
                }

                BotUtil.sendMessage(channel, embed.build());
            }
        } catch (IllegalArgumentException e) {
            BotUtil.sendTemporaryMessage(channel, "Invalid integer, please provide a valid number for that feed.", 10);
        }
    }
}
