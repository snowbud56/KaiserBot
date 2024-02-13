package com.snowbud56.utils.messages;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class MessageAction {

    private Action action;
    private Message message;
    private MessageEmbed messageEmbed;
    private String value;

    private MessageChannel channel;

    public MessageAction(Action action) {
        this.action = action;
    }

    public MessageAction message(Message message) {
        this.message = message;
        return this;
    }

    public MessageAction embedMessage(MessageEmbed message) {
        this.messageEmbed = message;
        return this;
    }

    public MessageAction value(String value) {
        this.value = value;
        return this;
    }

    public MessageAction channel(MessageChannel channel) {
        this.channel = channel;
        return this;
    }


    public Action getAction() {
        return action;
    }

    public boolean isEmbedMessage() {
        return messageEmbed != null;
    }

    public Message getMessage() {
        return message;
    }

    public MessageEmbed getMessageEmbed() {
        return messageEmbed;
    }

    public String getValue() {
        return value;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public enum Action {
        EDIT,
        DELETE,
        SEND
    }
}
