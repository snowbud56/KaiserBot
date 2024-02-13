package com.snowbud56.command;

/*
* Created by snowbud56 on April 16, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.KaiserBot;
import com.snowbud56.config.Config;
import com.snowbud56.rssfeed.feeds.Feed;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandGroupData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class CommandBase implements Command {
    protected CommandData commandData;
    protected final String commandName;

    //Set every time the command is ran; can be used in child classes without needing to reference SlashCommandEvent.
    protected MessageChannel channel;
    protected Guild guild;

    public CommandBase(String commandName) {
        this.commandName = commandName;
        commandData = new CommandData(commandName, getDescription());
    }

    @Override
    public String getCommandName() {
        return commandName;
    }

    protected void reply(SlashCommandEvent event, boolean hiddenFromOthers, String message) {
        event.reply(message).setEphemeral(hiddenFromOthers).queue((msg) -> {
            if (!hiddenFromOthers)
                msg.deleteOriginal().queueAfter(30, TimeUnit.SECONDS);
        });
    }

    protected void editReply(SlashCommandEvent event, String message) {
        event.getHook().editOriginal(message).queue();
    }

    protected void defer(SlashCommandEvent event, boolean hiddenFromOthers) {
        event.deferReply(hiddenFromOthers).queue();
    }

    protected abstract String getDescription();

    @Override
    public void onButtonPress(Feed feed, ButtonClickEvent event) {}

    @Override
    public void setChannel(MessageChannel channel) {
        this.channel = channel;
    }

    @Override
    public void setGuild(Guild guild) {
        this.guild = guild;
    }
}
