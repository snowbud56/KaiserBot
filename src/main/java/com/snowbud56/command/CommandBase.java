package com.snowbud56.command;

/*
* Created by snowbud56 on April 16, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.KaiserBot;
import com.snowbud56.config.Config;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class CommandBase implements Command {
    private List<String> aliases;
    protected String aliasUsed;
    protected Guild guild;

    public CommandBase(String...aliases) {
        this.aliases = Arrays.asList(aliases);
    }

    @Override
    public Collection<String> getAliases() {
        return aliases;
    }


    @Override
    public void setAliasUsed(String alias) {
        this.aliasUsed = alias;
    }

    @Override
    public void setGuild(Guild guild) {
        this.guild = guild;
        if (this.guild == null) this.guild = KaiserBot.getJDA().getGuildById(Config.getInstance().getString("guildID"));
    }
}
