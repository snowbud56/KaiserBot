package com.snowbud56.rssfeed.feeds;

/*
 * Created by snowbud56 on June 05, 2020
 * Do not change or use this code without permission
 */

import com.snowbud56.KaiserBot;
import com.snowbud56.config.Config;
import com.snowbud56.rssfeed.FeedMessage;
import com.snowbud56.rssfeed.FeedReader;
import com.snowbud56.utils.BotUtil;
import com.snowbud56.utils.TimeUnit;
import com.snowbud56.utils.TimeUtil;
import com.snowbud56.utils.managers.LogManager;
import net.dv8tion.jda.api.entities.TextChannel;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Feed {

    private int ID;
    protected final FeedSettings settings = new FeedSettings();
    private FeedReader feedReader;
    private long nextCheck, checkCooldown;
    protected TextChannel channel;
    private Thread runningThread;

    public Feed(int id) {

        //Sets up config and variables
        Config config = Config.getInstance();
        JSONObject feedConfig = (JSONObject) config.getJSONArray("feeds").get(id);
        if (feedConfig == null) {
            LogManager.logConsole("Unable to initialize a feed: ID not found.", true, true);
            return;
        }

        if (feedConfig.getInt("id") != id) {
            LogManager.logConsole("Unable to initialize feed \"" + feedConfig.getString("name") + "\": ID mismatch.", true, true);
            return;
        }

        this.settings.name = feedConfig.getString("name");
        this.ID = id;
        feedReader = new FeedReader(this);
        this.channel = KaiserBot.getJDA().getTextChannelById(feedConfig.getString("channelID"));
        checkCooldown = feedConfig.getLong("timeBetweenChecks");


        //Multiple URL Support
        ArrayList<String> URLList = new ArrayList<>();
        feedConfig.getJSONArray("urls").iterator().forEachRemaining((url) ->
                URLList.add(url.toString())
        );
        this.settings.URLs = URLList;

        //Notification filters
        ArrayList<String> notifyFilters = new ArrayList<>();
        feedConfig.getJSONArray("NotifyFilters").iterator().forEachRemaining((filter) ->
            notifyFilters.add(filter.toString())
        );
        this.settings.notifyFilters = notifyFilters;

        //Sends message if the feed has been initialized ONLY if the feed is not LiveIncidentFeed
        if (!(this instanceof LiveIncidentFeed))
            BotUtil.sendTemporaryMessage(channel, "This feed has been initialized", 10);

        //Starts the thread and forces an update
        nextCheck = 0L;
        startThread();
    }

    private void startThread() {

        runningThread = new Thread() {

            @Override
            public void run() {

                //makes sure it always runs
                int currentURL = 0;
                while (true) {
                    try {

                        //should the feed update?
                        if (checkForUpdate()) {

                            //set to be able to find how long it took to update
                            long startedUpdating = System.currentTimeMillis();

                            //forces a check for the feed and sets the cooldown for the next check
                            getReader().readFeed(currentURL, true);
                            setNextCheck(System.currentTimeMillis() + checkCooldown - 1);

                            //Chooses the next url to update
                            if (currentURL + 1 == getURL().size()) {
                                currentURL = 0;
                            } else {
                                currentURL++;
                            }

                            //logs how long it took to update
                            long timeUpdated = System.currentTimeMillis() - startedUpdating;
                            LogManager.logConsole("Took " + TimeUtil.getDuration(TimeUnit.FIT, timeUpdated) + " to update", false, false);
                        }

                        //makes the thread wait the minimum cooldown for all threads before updating again
                        sleep(100);
                    } catch (InterruptedException e) {
                        interrupt();
                    }
                }
            }
        };

        //starts the thread
        runningThread.start();
    }

    public void stopThread() {
        runningThread.interrupt();
    }

    public void forceCheck() {
        nextCheck = System.currentTimeMillis() - checkCooldown;
    }

    public FeedReader getReader() {
        return feedReader;
    }

    public boolean isEnabled() {
        return settings.enabled;
    }

    public void setEnabled(boolean enabled) {
        settings.enabled = enabled;
        if (settings.enabled) {
            BotUtil.sendMessage(channel, "This feed has been enabled.");
            startThread();
        } else {
//            BotUtil.sendMessage(channel, "This feed has been disabled.");
            stopThread();
        }
    }

    public boolean checkForUpdate() {
        if (!settings.enabled) return false;

        if (System.currentTimeMillis() > settings.timeToStartNotifying) {
            settings.notifying = true;
        }

        return System.currentTimeMillis() > nextCheck;
    }

    public HashMap<String, Object> getProperties() {
        HashMap<String, Object> properties = new HashMap<>();

        properties.put("1. ID", getID());
        properties.put("2. Enabled", isEnabled());
        properties.put("3. Name", getName());
        properties.put("4. URL", getURL());
        properties.put("5. Check cooldown", TimeUtil.getDuration(TimeUnit.FIT, getCheckCooldown()) + " (" + getCheckCooldown() + "ms)");
        properties.put("6. Next check", TimeUtil.getDuration(TimeUnit.FIT, getNextCheck() - System.currentTimeMillis()));
        properties.put("7. Notify Filters", getNotifyFilters().toString().replace("[", "").replace("]","").replace(", ", "\n"));

        int i = 8;
        if (getSpecificProperties() != null) {
            for (Map.Entry<String, Object> entry : getSpecificProperties().entrySet()) {
                properties.put(i + ". " + entry.getKey(), entry.getValue());
                i++;
            }
        }

        return properties;
    }

    public void setNextCheck(long nextCheck) {
        this.nextCheck = nextCheck;
    }

    public long getNextCheck() {
        return nextCheck;
    }

    public String getName() {
        return settings.name;
    }

    public long getCheckCooldown() {
        return checkCooldown;
    }

    public abstract HashMap<String, Object> getSpecificProperties();

    public abstract void sendMessages(List<FeedMessage> messageList);

    public int getID() {
        return ID;
    }

    public List<String> getURL() {
        return settings.URLs;
    }

    public ArrayList<String> getNotifyFilters() {
        return settings.notifyFilters;
    }

    public ArrayList<String> getDoNotSendFilters() {
        return settings.doNotSendFilters;
    }

    public void setNotifying(boolean notifying) {
        settings.notifying = notifying;
    }

    public void setTimeToNotify(long timeToNotify) {
        settings.timeToStartNotifying = timeToNotify;
    }

    public FeedSettings getSettings() {
        return settings;
    }
}
