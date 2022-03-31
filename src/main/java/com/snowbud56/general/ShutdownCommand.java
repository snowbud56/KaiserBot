package com.snowbud56.general;

import com.snowbud56.KaiserBot;
import com.snowbud56.command.CommandBase;
import com.snowbud56.config.Config;
import com.snowbud56.utils.TimeUnit;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

public class ShutdownCommand extends CommandBase {

    public ShutdownCommand() {
        super("shutdown", "stop");
    }

    @Override
    public void execute(Member member, MessageChannel channel, String[] args) {
        if (member.getUser().getId().equals(Config.getInstance().getString("ownerID"))) {
            channel.sendMessage("Shutting down after running for " + KaiserBot.getTimeSinceStart(TimeUnit.FIT)).complete();
            KaiserBot.shutdown();
        }
    }
}
