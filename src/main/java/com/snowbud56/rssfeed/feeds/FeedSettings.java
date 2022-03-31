package com.snowbud56.rssfeed.feeds;

import java.util.ArrayList;
import java.util.List;

public class FeedSettings {
    protected boolean enabled = true;
    protected String name;
    protected List<String> URLs;

    protected ArrayList<String> notifyFilters;
    protected ArrayList<String> doNotSendFilters;

    public boolean notifying = true;
    public long timeToStartNotifying = 0L;
}
