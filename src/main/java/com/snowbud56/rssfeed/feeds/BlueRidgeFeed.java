package com.snowbud56.rssfeed.feeds;

/*
 * Created by snowbud56 on November 29, 2020
 * Do not change or use this code without permission
 */

import com.snowbud56.KaiserBot;
import com.snowbud56.utils.TimeUtil;
import com.snowbud56.utils.managers.LogManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


public class BlueRidgeFeed {

    //Stores whether it is currently refreshing. Will not allow two refreshes to happen at the same time.
    private static boolean isRefreshing = false;

    // Set to store the URLs of images that have already been downloaded
    private static final Set<String> downloadedImages = ConcurrentHashMap.newKeySet();

    // Set to store the URLs of images that have already been uploaded
    private static final Set<String> uploadedImages = ConcurrentHashMap.newKeySet();

    // The ID of the Discord text channel to upload the images to
    private static final String CHANNEL_ID = "965398262792196146";

    public BlueRidgeFeed() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            refresh(KaiserBot.getJDA(), true, null);
        }, 0, 30, TimeUnit.MINUTES);
    }

    public static boolean refresh(JDA jda, boolean firstLoad, String bandToSearch) {

        //If it is currently refreshing, do not refresh again.
        if (isRefreshing) return false;

        //Setting when it starts refreshing and logs to console
        isRefreshing = true;
        Long timeStarted = System.currentTimeMillis();
        LogManager.logConsole("Refreshing Blue Ridge Announcements...", false, false);

        try {
            // The URL of the webpage with the images
            String url = "https://blueridgerockfest.com/lineup/";

            // Connect to the website and get the html
            Document doc = Jsoup.connect(url).get();

            // Find all elements with the "img" tag
            Elements images = doc.getElementsByTag("img");

            // For each element, get the src url and download the image if it has not been downloaded before
            // and the file name contains "artist-announcement"
            for (Element image : images) {
                String imgUrl = image.absUrl("src");
                if ((!downloadedImages.contains(imgUrl) && imgUrl.toLowerCase().contains("artist-announcement"))
                        || (bandToSearch != null && imgUrl.toLowerCase().contains(bandToSearch.toLowerCase().replace(" ", "-")))) {

                    //Makes sure that it will only download and upload the image to Discord when it should
                    if (!firstLoad || bandToSearch != null) {
                        String fileName = downloadImage(imgUrl, firstLoad);
                        uploadImage(jda, fileName);
                    }

                    //Adds the URL to the downloaded images list so it doesn't try and download the image again.
                    downloadedImages.add(imgUrl);
                }
            }
        } catch (Exception e) {
            LogManager.logConsole("Blue Ridge Failed to update: " + e, true, true);
        }

        //Logging that it's completed refreshing and tells how long it took.
        isRefreshing = false;
        LogManager.logConsole("Finished refreshing Blue Ridge Announcements in " + TimeUtil.getDuration(com.snowbud56.utils.TimeUnit.FIT, System.currentTimeMillis() - timeStarted), false, false);
        return true;
    }

    private static String downloadImage(String imageUrl, boolean shouldDelete) throws IOException {
        // Create a URL object for the image url
        URL url = new URL(imageUrl);

        // Open a connection to the image url and get the input stream
        InputStream in = url.openStream();

        // Generate a file name for the image
        String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

        // Create a folder to store the images if it does not already exist
        File folder = new File("Announcements Buffer");
        if (!folder.exists()) {
            folder.mkdir();
        }

        // Create an output stream to save the image to a file in the Announcements Buffer folder
        FileOutputStream out = new FileOutputStream(new File(folder, fileName));

        // Write the image to the file
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
        }

        // Close the input and output streams
        in.close();
        out.close();

        //Adds the file to the downloaded images
        downloadedImages.add(imageUrl);

        if (shouldDelete) {
            File file = new File("Announcements Buffer/" + fileName);
            file.delete();
        }

        return fileName;
    }

    private static void uploadImage(JDA jda, String fileName) throws IOException {

        // Get the Discord text channel to upload the image to
        TextChannel channel = jda.getTextChannelById(CHANNEL_ID);

        // Read the image file into a byte array
        File file = new File("Announcements Buffer/" + fileName);
        byte[] fileBytes = new byte[(int) file.length()];
        FileInputStream in = new FileInputStream(file);
        in.read(fileBytes);
        in.close();

        // Send a message to the Discord text channel with the image attached
        channel.sendMessage("<@&888096661551857715>\nARTIST ANNOUNCEMENT: " + fileName.substring(0, fileName.indexOf("-Artist")).replace("-", " ")).addFile(fileBytes, fileName).queue();

        //Delete the image file
        file.delete();

        //Adds file to uploaded images list
        uploadedImages.add(fileName);
    }

    public static void reannounceAllBands() {
        downloadedImages.clear();
        refresh(KaiserBot.getJDA(), false, null);
    }
}
