package com.snowbud56;

/*
* Created by snowbud56 on April 15, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.command.CommandManager;
import com.snowbud56.config.Config;
import com.snowbud56.rssfeed.FeedManager;
import com.snowbud56.rssfeed.feeds.FeedInitializer;
import com.snowbud56.utils.TimeUnit;
import com.snowbud56.utils.TimeUtil;
import com.snowbud56.utils.TimingUtil;
import com.snowbud56.utils.managers.LogManager;
import com.snowbud56.utils.messages.MessageHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.File;
import java.security.CodeSource;
import java.util.EnumSet;

public class KaiserBot extends ListenerAdapter {

    private static JDA JDA;
    private static long timeStarted;
    private static Thread runningThread;
    private static MessageChannel logChannel;

    public static void main(String[] args) {

        TimingUtil.startTiming("startBot");

        try {
            //Setting up config
            CodeSource codeSource = KaiserBot.class.getProtectionDomain().getCodeSource();
            new Config(new File(new File(codeSource.getLocation().toURI().getPath()).getParentFile().getPath(), "botsettings.json"));

            //setting up JDA
            JDABuilder builder = JDABuilder.createDefault(Config.getInstance().getString("token"));
            builder.setAutoReconnect(true);
            builder.setStatus(OnlineStatus.ONLINE);
            builder.setActivity(Activity.watching("over the server for " + TimeUtil.getDuration(TimeUnit.FIT, 0L)));

            JDA = builder
                    .disableCache(EnumSet.of(
                            CacheFlag.EMOTE,
                            CacheFlag.ACTIVITY,
                            CacheFlag.CLIENT_STATUS))
                    .enableCache(EnumSet.of(
                            CacheFlag.VOICE_STATE))
                    .build().awaitReady();

            //Setting variables
            timeStarted = System.currentTimeMillis();
            logChannel = JDA.getTextChannelById(Config.getInstance().getString("logChannel"));

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        LogManager.logConsole("Logged in as " + getJDA().getSelfUser().getName() + "#" + getJDA().getSelfUser().getDiscriminator() + "!", false, true);

        startThreads();
        registerListeners(JDA, new CommandManager());

        FeedInitializer.initializeFeeds();

        TimingUtil.Timing time = TimingUtil.stopTiming("startBot");
        LogManager.logConsole("Bot started in " + TimeUtil.getDuration(TimeUnit.FIT, time.getMilliDuration()), false, true);
    }

    private static void registerListeners(JDA jda, ListenerAdapter... listeners) {
        for (ListenerAdapter listener : listeners) {
            jda.addEventListener(listener);
        }
    }

    public static void shutdown() {
        logChannel.sendMessage(TimeUtil.getDate() + ": Stopped").queue();
        runningThread.interrupt();
        MessageHandler.stopThread();
        FeedManager.stopThreads();
        JDA.shutdown();
    }

    private static void startThreads() {
        logChannel.sendMessage(TimeUtil.getDate() + ": Started").queue();
        runningThread = new Thread() {
            @SuppressWarnings("InfiniteLoopStatement")
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(60000);
                        JDA.getPresence().setActivity(Activity.watching("over the server for " + getTimeSinceStart(TimeUnit.FIT)));
                    } catch (InterruptedException e) {
                        interrupt();
                    }
                }
            }
        };
        runningThread.start();

        MessageHandler.startThread();
    }

    public static net.dv8tion.jda.api.JDA getJDA() {
        return JDA;
    }

    public static MessageChannel getLogChannel() {
        return logChannel;
    }

    public static String getTimeSinceStart(TimeUnit unit) {
        return TimeUtil.getDuration(unit, System.currentTimeMillis() - timeStarted);
    }
}