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

    private List<String> linksAlreadySent = new ArrayList<>();
    private int titleListLimit = 6;

    public MusicReleasedFeed() {
        super(1);

        this.setEnabled(false);
    }

    public void testAddLink() {
        linksAlreadySent.add("1234567891234567");
    }

    @Override
    public void sendMessages(List<FeedMessage> messageList) {
        for (FeedMessage message : messageList) {
            String postID = message.getLink().substring(message.getLink().indexOf("/posts/") + 7, message.getLink().lastIndexOf("/"));
             if (!linksAlreadySent.contains(postID)) {
                 sendMessage(message);
                 linksAlreadySent.add(postID);
             }
        }

        checkTitleLimit();
    }

    private void checkTitleLimit() {
        if (linksAlreadySent.size() <= titleListLimit) return;

        while (linksAlreadySent.size() > titleListLimit) {
            linksAlreadySent.remove(linksAlreadySent.size() - 1);
        }
        System.out.println(linksAlreadySent);
    }

    private void sendMessage(FeedMessage msg) {
        StringBuilder messageBuilder = new StringBuilder();
        boolean notified = false;

        if (settings.notifying) {

            //notifies me based off of the filters in botsettings.json
            for (String notifyFilter : settings.notifyFilters) {
                if (msg.getDescription().toLowerCase().contains(notifyFilter.toLowerCase()) && !notified) {
                    messageBuilder.append("<@&888096661551857715>\n");
                    notified = true;
                }
            }
        }

        if (!notified) return;

        //builds the message replacing unwanted characters
//        messageBuilder.append(msg.getDescription().replace("&#8211;", "-").replace("&#8217;", "'").replace("&#038;", "&"))
//                .append("\n")
//                .append(msg.getLink());
        messageBuilder.append(msg.getDescription().substring(0, msg.getDescription().indexOf("<br>")))
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
