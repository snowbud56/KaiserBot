package com.snowbud56.moderation.commands;

/*
 * Created by snowbud56 on January 15, 2021
 * Do not change or use this code without permission
 */

import com.snowbud56.command.CommandBase;
import com.snowbud56.utils.BotUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

public class ClearCommand extends CommandBase {

    public ClearCommand() {
        super("clear");
    }

    @Override
    public void execute(Member member, MessageChannel channel, String[] args) {
        if (args.length == 0) {
            try {
                channel.getHistoryFromBeginning(100).complete().getRetrievedHistory()
                        .forEach(BotUtil::deleteMessage);
            } catch (Exception ignored) {}
        } else {
            try {
                channel.getHistoryBefore(channel.getLatestMessageId(), Integer.parseInt(args[0])).complete().getRetrievedHistory()
                        .forEach(BotUtil::deleteMessage);
            } catch (Exception e) {
                BotUtil.sendMessage(channel, "That is not a valid number! Usage: /clear <amount>");
            }
        }
    }
}
