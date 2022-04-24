package com.snowbud56.command;

/*
* Created by snowbud56 on April 16, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.rssfeed.feeds.Feed;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Collection;

public interface Command {

    void execute(SlashCommandEvent event);

    void setGuild(Guild guild);
    void setChannel(MessageChannel channel);

    CommandData getCommandData();
    String getCommandName();

    void onButtonPress(Feed feed, ButtonClickEvent event);

}
