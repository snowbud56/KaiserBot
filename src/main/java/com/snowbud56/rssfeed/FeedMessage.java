package com.snowbud56.rssfeed;

/*
 * Created by snowbud56 on June 05, 2020
 * Do not change or use this code without permission
 */

import com.snowbud56.rssfeed.feeds.Feed;

public class FeedMessage {

    private Feed feed;
    private String title;
    private String description;
    private String timeStamp;
    private String link;
    private boolean notified;

    public FeedMessage(Feed feed, String item) {
        this.feed = feed;
        item = item.replace("<![CDATA[", "").replace("]]>", "");
        if (item.contains("<title>")) {
            title = item.substring(item.indexOf("<title>"));
            title = title.substring(0, title.indexOf("</title>")).replace("<title>", "").replace("</title>", "");
//        LogManager.logConsole("title: " + title, false);
        }

        if (item.contains("<link>")) {
            link = item.substring(item.indexOf("<link>"));
            link = link.substring(0, link.indexOf("</link>")).replace("<link>", "").replace("</link>", "");
//        LogManager.logConsole("link: " + link, false);
        }

        if (item.contains("<link rel=")) {
            link = item.substring(item.indexOf("<link rel="));
            link = link.substring(0, link.indexOf("href=\"")).replace("href=\"", "").replace("\"/>", "");
//        LogManager.logConsole("link: " + link, false);
        }

        if (item.contains("<description>")) {
            description = item.substring(item.indexOf("<description>"));
            description = description.substring(0, description.indexOf("</description>")).replace("<description>", "").replace("</description>", "");
//        LogManager.logConsole("desc: " + description, false);
        }


        if (item.contains("<pubDate>")) {
            timeStamp = item.substring(item.indexOf("<pubDate>"));
            timeStamp = timeStamp.substring(0, timeStamp.indexOf("</pubDate>")).replace("<pubDate>", "").replace("</pubDate>", "");
//        LogManager.logConsole("timeStamp: " + timeStamp, false);
        }

        if (item.contains("<published>")) {
            timeStamp = item.substring(item.indexOf("<published>"));
            timeStamp = timeStamp.substring(0, timeStamp.indexOf("</published>")).replace("<published>", "").replace("</published>", "");
//        LogManager.logConsole("timeStamp: " + timeStamp, false);
        }

    }

    public void setNotified(boolean notified) {
        this.notified = notified;
    }

    public boolean wasNotified() {
        return notified;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getLink() {
        return link;
    }

    public Feed getFeed() {
        return feed;
    }
}
