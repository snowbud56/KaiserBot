package com.snowbud56.general;

import com.snowbud56.KaiserBot;
import com.snowbud56.command.CommandBase;
import com.snowbud56.config.Config;
import com.snowbud56.utils.TimeUnit;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class ShutdownCommand extends CommandBase {

    public ShutdownCommand() {
        super("stop");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        if (event.getMember().getUser().getId().equals(Config.getInstance().getString("ownerID"))) {
            event.reply("Shutting down after running for " + KaiserBot.getTimeSinceStart(TimeUnit.FIT)).complete();
            KaiserBot.shutdown();
        }
    }

    @Override
    public CommandData getCommandData() {
        return commandData;
    }

    @Override
    protected String getDescription() {
        return "Shuts down the bot.";
    }
}
