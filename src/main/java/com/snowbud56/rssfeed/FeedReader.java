package com.snowbud56.rssfeed;

/*
 * Created by snowbud56 on June 05, 2020
 * Do not change or use this code without permission
 */

import com.snowbud56.rssfeed.feeds.Feed;
import com.snowbud56.utils.managers.LogManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

public class FeedReader {

    private Feed feed;
    private boolean isReading = false;
    public ArrayList<FeedMessage> messages = new ArrayList<>();

    public FeedReader(Feed feed) {
        this.feed = feed;
    }

    public void readFeed(int URLindex, boolean sendMessages) {
        if (isReading) {
            LogManager.logConsole("Feed " + feed.getName() + " is already checking, cannot check until the current one is complete.", false, true);
            return;
        }

        isReading = true;

        messages.clear();

        LogManager.logConsole("Reading " + feed.getName() + "...", false, false);


        ArrayList<FeedMessage> flippedMessages = new ArrayList<>();

        try {
            //Establishes a connection with the website.
            URLConnection urlConnection = new URL(feed.getURL().get(URLindex)).openConnection();
            urlConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            InputStream is = urlConnection.getInputStream();

            //Checks to make sure it's using the correct InputStream for the type of file it is.
            //Shouldn't ever be gzip, but I have it here anyway.
            if("gzip".equals(urlConnection.getContentEncoding())){
                is = new GZIPInputStream(is);
            }

            //Gets the input from the website and puts it into a StringBuilder.
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder replySB = new StringBuilder();
            while ((line = in.readLine()) != null)
                replySB.append(line);

            //Formats the reply so FeedMessage can parse the String.
            String reply = replySB.toString();
            if (!reply.contains("<item>")) return;
            String[] msgs = reply.split("<item>");
            for (String msg : msgs) {
                if (msg.equals(msgs[0])) continue;
                String message = msg;
                message = message.substring(0, message.length() - 7);
                flippedMessages.add(new FeedMessage(feed, message));
            }

            //Flips the message order so that they're sent oldest to newest.
            while (flippedMessages.size() > 0) {
                messages.add(flippedMessages.get(flippedMessages.size() - 1));
                flippedMessages.remove(flippedMessages.size() - 1);
            }

            //Sends messages to Feed classes.
            if (sendMessages) sendMessages();

        } catch (Exception e) {
            e.printStackTrace();
            LogManager.logConsole("Unable to access feed; " + e, true, true);
        } finally {
            isReading = false;
        }
    }

    //Sends messages to Feed classes.
    void sendMessages() {
            feed.sendMessages(messages);
    }
}
