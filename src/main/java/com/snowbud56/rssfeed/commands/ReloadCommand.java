package com.snowbud56.rssfeed.commands;

/*
 * Created by snowbud56 on September 17, 2020
 * Do not change or use this code without permission
 */

import com.snowbud56.command.CommandBase;
import com.snowbud56.config.Config;
import com.snowbud56.rssfeed.FeedManager;
import com.snowbud56.rssfeed.feeds.FeedInitializer;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class ReloadCommand extends CommandBase {

    public ReloadCommand() {
        super("reload");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        if (event.getMember().getId().equals(Config.getInstance().getString("ownerID"))) {
            FeedManager.clearFeeds();
            Config.reloadConfig();
            FeedInitializer.initializeFeeds();
            event.reply("Reloaded feeds :+1:").queue();
        } else {
            event.reply("You don't have permission to execute this command, only the other can execute this command.").queue();
        }
    }

    @Override
    public CommandData getCommandData() {
        return commandData;
    }

    @Override
    protected String getDescription() {
        return "Reloads feeds from config file.";
    }
}
