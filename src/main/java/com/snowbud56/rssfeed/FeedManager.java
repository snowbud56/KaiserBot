package com.snowbud56.rssfeed;

/*
 * Created by snowbud56 on June 05, 2020
 * Do not change or use this code without permission
 */

import com.snowbud56.rssfeed.feeds.Feed;
import com.snowbud56.utils.BotUtil;
import com.snowbud56.utils.managers.LogManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeedManager {

    //The list of feeds initialized
    private static List<Feed> feeds = new ArrayList<>();

    public static void addFeed(Feed feed) {
        feeds.add(feed);
    }

    public static void stopThreads() {
        for (Feed feed : feeds) {
            feed.stopThread();
        }
    }

    public static void clearFeeds() {
        feeds.clear();
    }

    public static Feed getFeed(int id) {
        for (Feed feed : feeds) {
            if (feed.getID() == id) {
                return feed;
            }
        }
        return null;
    }

    public static void sendFeedListMessage(MessageChannel channel) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle("Avaliable Feeds");
        embed.setColor(new Color(255, 254, 0));
        for (Feed feed : feeds) {
            embed.addField(
                    feed.getID() + ". " + feed.getName(),
                    feed.getURL().toString().replace("[", "").replace("]", "").replace(", ", "\n"), false);
        }
        BotUtil.sendTemporaryMessage(channel, embed.build(), 10);
    }
}
