package com.snowbud56.rssfeed;

/*
 * Created by snowbud56 on June 05, 2020
 * Do not change or use this code without permission
 */

import com.snowbud56.rssfeed.feeds.Feed;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.components.Button;

import java.util.ArrayList;
import java.util.List;

public class FeedManager {

    //The list of feeds initialized
    private static List<Feed> feeds = new ArrayList<>();

    public static void addFeed(Feed feed) {
        feeds.add(feed);
    }

    public static void stopThreads() {
        feeds.forEach(Feed::stopThread);
    }

    public static void clearFeeds() {
        feeds.clear();
    }

    public static Feed getFeed(int id) {
        for (Feed feed : feeds) {
            if (feed.getID() == id)
                return feed;
        }
        return null;
    }

    public static void sendFeedListMessage(SlashCommandEvent event, String commandUsed) {
        List<Button> buttons = new ArrayList<>();
        for (Feed feed : feeds) {
            buttons.add(Button.primary(event.getUser().getId() + ":" + feed.getID() + ":" + commandUsed, feed.getName()));
        }
        event.reply("Here are the list of feeds:").addActionRow(buttons).queue();
    }
}
