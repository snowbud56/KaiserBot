package com.snowbud56.rssfeed.feeds;

/*
 * Created by snowbud56 on September 17, 2020
 * Do not change or use this code without permission
 */

import com.snowbud56.rssfeed.FeedManager;

public class FeedInitializer {

    public static void initializeFeeds() {
        FeedManager.addFeed(new LiveIncidentFeed());
        FeedManager.addFeed(new MusicReleasedFeed());
    }
}