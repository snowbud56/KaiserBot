package com.snowbud56.command;

/*
* Created by snowbud56 on April 16, 2018
* Do not change or use this code without permission
*/

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.Collection;

public interface Command {

    void execute(Member member, MessageChannel channel, String[] args);

    Collection<String> getAliases();

    void setAliasUsed(String s);
    void setGuild(Guild guild);

}
