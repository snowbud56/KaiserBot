package com.snowbud56.moderation.commands;

/*
 * Created by snowbud56 on January 15, 2021
 * Do not change or use this code without permission
 */

import com.snowbud56.command.CommandBase;
import com.snowbud56.utils.BotUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class ClearCommand extends CommandBase {

    public ClearCommand() {
        super("clear");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        Integer amount;
        if (event.getOption("amount") == null)
            amount = 100;
        else
            amount = (int) event.getOption("amount").getAsLong();
//        event.deferReply(true).queue();

        channel.getHistory().retrievePast(amount).queue(BotUtil::deleteMessages);

        event.reply(":+1:").queue();

    }

    @Override
    public CommandData getCommandData() {
        commandData.addOption(OptionType.INTEGER, "amount", "Number of messages to delete.");
        return commandData;
    }

    @Override
    protected String getDescription() {
        return "Clears messages in the channel.";
    }
}
