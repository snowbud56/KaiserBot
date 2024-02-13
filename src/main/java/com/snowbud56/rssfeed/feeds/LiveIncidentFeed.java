package com.snowbud56.rssfeed.feeds;

/*
 * Created by snowbud56 on November 19, 2020
 * Do not change or use this code without permission
 */

import com.snowbud56.rssfeed.FeedMessage;
import com.snowbud56.utils.BotUtil;
import com.snowbud56.utils.TimeUtil;
import com.snowbud56.utils.managers.LogManager;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LiveIncidentFeed extends Feed {

    private static int numOfIncidents = 0;

    public LiveIncidentFeed() {
        super(0);
    }

    @Override
    public void sendMessages(List<FeedMessage> messageList) {

        //setting variables
        Map<String, StringBuilder> messagesBuilders = new HashMap<>();
        Map<Message, Boolean> shouldDelete = new HashMap<>();
        boolean noIncidents = true;

        //fill messageBuilders hashmap with the messages from rss feed
        for (FeedMessage msg : messageList) {

            //doesn't send if the message has "pending" in it
            if (msg.getDescription().toLowerCase().contains("pending"))
                continue;

            //filters out medical emergencies but still leaves everything in the first item in the filter
            if ((msg.getTitle().toLowerCase().contains("medical emergency") || msg.getTitle().toLowerCase().contains("routine transfer")) && !msg.getDescription().toLowerCase().contains(getNotifyFilters().get(0).toLowerCase()))
                continue;

            //sets key for the messageBuilders hashmap so all of the same incidents are grouped together
            String mapKey = getFormattedIncidentTitle(msg, true);

            //sets whether or not I should be notified based off of the filters in the botsettings.json
            boolean notify = false;
            if (settings.notifying) {
                for (String filter : settings.notifyFilters)
                    if ((msg.getDescription().toLowerCase().contains(filter.toLowerCase()) || msg.getTitle().toLowerCase().contains(filter.toLowerCase()))
                            && !(msg.getTitle().toLowerCase().contains("medical emergency") || msg.getTitle().toLowerCase().contains("routine transfer"))) {
                        notify = true;
                        break;
                    }
            }

            //gets the string builder from the map (if present) or makes a new string builder if it hasn't been initiated yet
            StringBuilder stringBuilder = messagesBuilders.getOrDefault(mapKey, new StringBuilder((notify ? "<@142851757914062848>\n" : "") + getFormattedIncidentTitle(msg, false)));

            //makes a new string builder for all of the apparatuses responding to the incident
            StringBuilder messageContent = new StringBuilder();
            String[] splitMessage = msg.getDescription().replace("amp;", "").replace("<br>", ";").replace("br&gt;", ";").replace("&lt;", "").split("; ");
            for (int i = 2; i < splitMessage.length; i++) {
                messageContent.append("\n").append(splitMessage[i].replace("br&gt", "").replace("&lt", ""));
            }

            //adds the apparatuses to the incident
            stringBuilder.append(messageContent);

            //puts the builder into the map
            messagesBuilders.put(mapKey, stringBuilder);
        }

        //Update channel topic and numOfIncidents variable if the number of incidents changed.
        if (messagesBuilders.size() != numOfIncidents) {
            channel.getManager().setTopic(messagesBuilders.size() + " active incident" + (messagesBuilders.size() == 1 ? "" : "s") + ".").queueAfter(5, TimeUnit.SECONDS);

            numOfIncidents = messagesBuilders.size();
            LogManager.logConsole("Updated topic with " + numOfIncidents + " incident" + (numOfIncidents == 1 ? "" : "s") + ".", false, false);
        }

        //update messages in the channel with new messages
        MessageHistory history = channel.getHistoryFromBeginning(100).complete();

        if (!history.isEmpty()) {

            //loops through every message in the channel
            history.getRetrievedHistory().forEach((message) -> {

                if (!message.getContentStripped().equalsIgnoreCase("https://lcwc911.us/live-incident-list")) {
                    shouldDelete.put(message, true);

                    //filters out the messages that the bot sends that aren't a part of the feed (ex. "Forcing a check for that feed...")
                    if ((message.getContentRaw().startsWith("__") || message.getContentRaw().startsWith("<@")) && message.getAuthor().isBot()) {

                        String[] messageSplit = message.getContentStripped().replace("@snowbud56 \n", "").replace("@snowbud56\n", "").replace("\n", ";").split(";");

                        //formats the message to be able to read the messagesBuilders map and get the builder for the incident
                        String compare = messageSplit[0] + " " + messageSplit[1] + (messageSplit.length >= 3 ? " " + messageSplit[2] : "");
                        compare = compare.replace("  ", " ");

                        StringBuilder builder = messagesBuilders.getOrDefault(compare, null);

                        //edits the message and removes it from the deletes and messagesBuilder map
                        if (builder != null) {
                            if (!message.getContentRaw().equalsIgnoreCase(builder.toString())) {
                                message.editMessage(builder.toString()).complete();
                            }
                            shouldDelete.put(message, false);
                            messagesBuilders.remove(compare);
                        }
                    }
                }
            });

            //deletes any message that isn't an incident anymore
            for (Map.Entry<Message, Boolean> entry : shouldDelete.entrySet()) {
                if (entry.getValue()) {
                    try {
                        BotUtil.deleteMessage(entry.getKey());
                    } catch (Exception ignored) {}
                } else noIncidents = false;
            }

            //sends any new incident and sends the title to the title checker
            for (StringBuilder builder : messagesBuilders.values()) {
                noIncidents = false;
                BotUtil.sendMessage(channel, builder.toString());
            }
        }

        //send "none :)" if there are no emergencies
        if (noIncidents) {
            BotUtil.sendMessage(channel, "None! :smile:");
        }

        if (numOfIncidents > 60) {
            setNextCheck(System.currentTimeMillis() + getCheckCooldown() + (numOfIncidents - 60) * 5000L);
            LogManager.logConsole("Too many incidents (" + numOfIncidents + "), the next check will be in " + TimeUtil.getDuration(com.snowbud56.utils.TimeUnit.FIT, getNextCheck() - System.currentTimeMillis()), false, false);
            BotUtil.sendMessage(channel, "Too many incidents (" + numOfIncidents + "), the next check will be in " + TimeUtil.getDuration(com.snowbud56.utils.TimeUnit.FIT, getNextCheck() - System.currentTimeMillis()));
        }
    }

    //don't worry about this complicated thing, it works. leave it alone.
    private String getFormattedIncidentTitle(FeedMessage msg, boolean plainText) {

        String string = (plainText ? "" : "__") + msg.getTitle() + (plainText ? " " : "__\n") +
                msg.getDescription().split(";")[0] + (plainText ? " " : "\n") + msg.getDescription().replace("amp;", "").split(";")[1];

        return string.replace("  ", " ").replace("  ", " ");
    }

    @Override
    public HashMap<String, Object> getSpecificProperties() {
        HashMap<String, Object> properties = new HashMap<>();

        properties.put("Number of Incidents", getNumOfIncidents());

        return properties;
    }

    public static int getNumOfIncidents() {
        return numOfIncidents;
    }
}