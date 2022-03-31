package com.snowbud56.moderation.commands;

import com.snowbud56.command.CommandBase;
import com.snowbud56.utils.BotUtil;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

public class SayCommand extends CommandBase {

    public SayCommand() {
        super("say");
    }
    @Override
    public void execute(Member member, MessageChannel channel, String[] args) {
        if (member.hasPermission(Permission.MESSAGE_MANAGE)) {
            if (args.length == 0) {
                BotUtil.sendTemporaryMessage(channel, "You need to provide a message for me to send! /say <message>", 10);
                return;
            }

            StringBuilder message = new StringBuilder();
            for (String arg : args) {
                message.append(arg).append(" ");
            }

            BotUtil.sendMessage(channel, message.toString());
        } else {
            BotUtil.sendTemporaryMessage(channel, "You don't have permission to execute this command.", 10);
        }
    }
}
