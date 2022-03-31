package com.snowbud56.utils.managers;

/*
* Created by snowbud56 on April 16, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.KaiserBot;
import com.snowbud56.utils.TimeUtil;

public class LogManager {

    public static void logConsole(String message, boolean error, boolean sendToChannel) {
        System.out.println("[" + TimeUtil.getDate() + "] " + (error ? "ERROR: " : "") + message.replace("*", ""));

        if (sendToChannel)
            KaiserBot.getLogChannel().sendMessage((error ? "ERROR: " : "") + message).queue();
    }
}
