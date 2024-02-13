package com.snowbud56.utils.messages;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.List;

public class MessageHandler {


    private static Thread runningThread;

    private static float numActionsInSecond = 1f;

    private static List<MessageAction> messageQueue = new ArrayList<>();

    public static boolean queueSendMessage(MessageChannel channel, String message) {
        MessageAction action = new MessageAction(MessageAction.Action.SEND).channel(channel).value(message);
        messageQueue.add(action);
        return true;
    }

    public static boolean queueSendMessage(MessageChannel channel, MessageEmbed embedMessage) {
        MessageAction action = new MessageAction(MessageAction.Action.SEND).channel(channel).embedMessage(embedMessage);
        messageQueue.add(action);
        return true;
    }

    public static boolean queueEditMessage(Message msg, String value) {
        MessageAction action = new MessageAction(MessageAction.Action.EDIT).message(msg).value(value);
        messageQueue.add(action);
        return true;
    }

    public static boolean queueDeleteMessage(Message msg) {
        MessageAction action = new MessageAction(MessageAction.Action.DELETE).message(msg);
        messageQueue.add(action);
        return true;
    }

    public static void stopThread() {
        runningThread.interrupt();
    }

    public static void startThread() {
        runningThread = new Thread() {
            @SuppressWarnings("InfiniteLoopStatement")
            @Override
            public void run() {
                while (true) {
                    try {
                        sleep(Math.round(((1000f / numActionsInSecond))));
                    } catch (InterruptedException e) {
                        this.interrupt();
                    }

                    if (!messageQueue.isEmpty()) {

                        //Gets the next action and removes it from the list
                        MessageAction action = messageQueue.get(0);
                        messageQueue.remove(action);

                        switch (action.getAction()) {

                            //Sending a message (Requires channel and either EmbedMessage or a String)
                            case SEND:
                                if (action.isEmbedMessage())
                                    action.getChannel().sendMessageEmbeds(action.getMessageEmbed()).queue();
                                else
                                    action.getChannel().sendMessage(action.getValue()).queue();
                                continue;

                            //Editing a message (Requires message and value)
                            case EDIT:
                                action.getMessage().editMessage(action.getValue()).queue();
                                continue;

                            //Deleting a message, only needs message
                            case DELETE:
                                action.getMessage().delete().queue();
                        }
                    }
                }
            }
        };
        runningThread.start();
    }

}
