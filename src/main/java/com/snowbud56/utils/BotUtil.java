package com.snowbud56.utils;

/*
* Created by snowbud56 on April 16, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.KaiserBot;
import com.snowbud56.utils.messages.MessageHandler;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class BotUtil {

    public static void sendMessage(MessageChannel channel, String message) {
//        channel.sendMessage(message).queue();
        MessageHandler.queueSendMessage(channel, message);
    }

    public static void sendMessage(MessageChannel channel, MessageEmbed message) {
        channel.sendMessageEmbeds(message).queue();
    }

//    public static Message sendMemberMessage(MessageChannel channel, Member member, String message) {
//        return channel.sendMessage(member.getAsMention() + " " + message).complete();
//    }

    public static Message sendTemporaryMessage(MessageChannel channel, String message, int secondsToDisplay) {
        Message msg = channel.sendMessage(message).complete();
        try {
            msg.delete().queueAfter(secondsToDisplay, TimeUnit.SECONDS);
        } catch (Exception ignored) {}
        return msg;
    }

    public static void sendTemporaryReply(SlashCommandEvent event, String message, int secondsToDisplay) {
        event.reply(message).queue((msg) ->
                msg.deleteOriginal().queueAfter(10, TimeUnit.SECONDS));
    }

    public static Message sendTemporaryMessage(MessageChannel channel, MessageEmbed embed, int secondsToDisplay) {
        Message msg = channel.sendMessage(embed).complete();
        try {
            msg.delete().queueAfter(secondsToDisplay, TimeUnit.SECONDS);
        } catch (Exception ignored) {}
        return msg;
    }

    public static boolean deleteMessage(Message message) {
        if (canDelete(message))
//            message.delete().queue();
            MessageHandler.queueDeleteMessage(message);
        return canDelete(message);
    }

    public static void deleteMessages(Collection<Message> messages) {
        for (Message msg : messages)
            deleteMessage(msg);
    }

    public static boolean canDelete(Message message) {
        if (message.isPinned())
            return false;

        if ((message.getChannel() instanceof PrivateChannel) && message.getAuthor() != KaiserBot.getJDA().getSelfUser())
            return false;

        return true;
    }
}
