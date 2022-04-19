package com.snowbud56.rssfeed.commands;

import com.snowbud56.command.CommandBase;
import com.snowbud56.rssfeed.FeedManager;
import com.snowbud56.rssfeed.feeds.MusicReleasedFeed;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

public class TestCommand extends CommandBase {

    public TestCommand() {
        super("test");
    }

    @Override
    public void execute(Member member, MessageChannel channel, String[] args) {
        ((MusicReleasedFeed) FeedManager.getFeed(1)).testAddLink();
    }
}
