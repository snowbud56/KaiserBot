package com.snowbud56.moderation.commands;

import com.snowbud56.command.CommandBase;
import com.snowbud56.utils.BotUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.concurrent.TimeUnit;

public class SayCommand extends CommandBase {

    public SayCommand() {
        super("say");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        if (event.getMember().hasPermission(Permission.MESSAGE_MANAGE) || (event.getChannel() instanceof PrivateChannel)) {
            BotUtil.sendMessage(channel, event.getOption("message").getAsString().replace("\\n", "\n"));
            event.reply(":+1:").queue((message) -> message.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
        } else {
            event.reply("You don't have permission to execute this command.").queue((message) -> message.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
        }
    }

    @Override
    public CommandData getCommandData() {
        return commandData.addOptions(
                new OptionData(OptionType.STRING, "message", "Message for the bot to say.")
                        .setRequired(true));
    }

    @Override
    protected String getDescription() {
        return "Makes the bot send a message of your liking.";
    }
}
