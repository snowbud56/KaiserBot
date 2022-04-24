package com.snowbud56.rssfeed.commands;

/*
 * Created by snowbud56 on June 12, 2020
 * Do not change or use this code without permission
 */

import com.snowbud56.command.CommandBase;
import com.snowbud56.rssfeed.FeedManager;
import com.snowbud56.rssfeed.feeds.Feed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class InfoCommand extends CommandBase {

    public InfoCommand() {
        super("info");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        FeedManager.sendFeedListMessage(event, commandName);
    }

    @Override
    public void onButtonPress(Feed feed, ButtonClickEvent event) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(new Color(255, 254, 0));
        embed.setTitle(feed.getName());

        HashMap<String, Object> feedProperties = feed.getProperties();

        ArrayList<String> propertyIDs = new ArrayList<>(feedProperties.keySet());

        Collections.sort(propertyIDs);

        propertyIDs.forEach((IDs) -> embed.addField(IDs, feedProperties.get(IDs).toString(), false));

        event.replyEmbeds(embed.build()).queue();
    }

    @Override
    public CommandData getCommandData() {
        return commandData;
    }

    @Override
    protected String getDescription() {
        return "Displays useful information about a feed.";
    }
}
