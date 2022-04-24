package com.snowbud56.utils;

/*
* Created by snowbud56 on April 16, 2018
* Do not change or use this code without permission
*/

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class BotUtil {

    public static Message sendMessage(MessageChannel channel, String message) {
        return channel.sendMessage(message).complete();
    }
    public static Message sendMessage(MessageChannel channel, MessageEmbed message) {
        return channel.sendMessage(message).complete();
    }

//    public static Message sendMemberMessage(MessageChannel channel, Member member, String message) {
//        return channel.sendMessage(member.getAsMention() + " " + message).complete();
//    }

    public static Message sendTemporaryMessage(MessageChannel channel, String message, int secondsToDisplay) {
        Message msg = channel.sendMessage(message).complete();
        try {
            msg.delete().delay(secondsToDisplay, TimeUnit.SECONDS).queue();
        } catch (Exception ignored) {}
        return msg;
    }

    public static Message sendTemporaryMessage(MessageChannel channel, MessageEmbed embed, int secondsToDisplay) {
        Message msg = channel.sendMessage(embed).complete();
        try {
            msg.delete().queueAfter(secondsToDisplay, TimeUnit.SECONDS);
        } catch (Exception ignored) {}
        return msg;
    }

    public static boolean deleteMessage(Message message) {
        if (canDelete(message)) message.delete().queue();
        return canDelete(message);
    }

    public static void deleteMessages(Collection<Message> messages) {
        for (Message msg : messages)
            deleteMessage(msg);
    }

    public static boolean canDelete(Message message) {
        if (message.isPinned()) {
            return false;
        }
        return true;
    }
}
