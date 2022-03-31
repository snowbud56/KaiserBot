package com.snowbud56.rssfeed.feeds;

/*
 * Created by snowbud56 on November 29, 2020
 * Do not change or use this code without permission
 */

import com.snowbud56.rssfeed.FeedMessage;
import com.snowbud56.utils.BotUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


//TODO This whole class is going to be remade to be better, I made this quickly and never got around to updating it.
public class MusicReleasedFeed extends Feed {

    private List<String> titlesAlreadySent = new ArrayList<>();
    private int titleListLimit = 20;

    public MusicReleasedFeed() {
        super(1);

//        this.setEnabled(false);
    }

    @Override
    public void sendMessages(List<FeedMessage> messageList) {
        for (FeedMessage message : messageList) {
             if (!titlesAlreadySent.contains(message.getTitle().toLowerCase())) {
                 sendMessage(message);
                 titlesAlreadySent.add(message.getTitle().toLowerCase());
             }
        }

        checkTitleLimit();
    }

    private void checkTitleLimit() {
        if (titlesAlreadySent.size() > titleListLimit) {
            while (titlesAlreadySent.size() > titleListLimit) {
                titlesAlreadySent.remove(titlesAlreadySent.size() - 1);
            }
        }
    }

    private void sendMessage(FeedMessage msg) {
        StringBuilder messageBuilder = new StringBuilder();
        boolean notified = false;

        if (settings.notifying) {

            //notifies me based off of the filters in botsettings.json
            for (String notifyFilter : settings.notifyFilters) {
                if (msg.getTitle().toLowerCase().contains(notifyFilter.toLowerCase()) && !notified) {
                    messageBuilder.append("<@142851757914062848>\n");
                    notified = true;
                }
            }
        }

        //builds the message replacing unwanted characters
        messageBuilder.append(msg.getTitle().replace("&#8211;", "-").replace("&#8217;", "'").replace("&#038;", "&"))
                .append("\n")
                .append(msg.getLink());

        //sends message to the channel and saves to variable
//        Message message = BotUtil.sendMessage(channel, messageBuilder.toString());
        BotUtil.sendMessage(channel, messageBuilder.toString());

//        //pins the message if I was notified
//        if (notified)
//            message.pin().queue();
    }

    @Override
    public HashMap<String, Object> getSpecificProperties() {
        return null;
    }
}
