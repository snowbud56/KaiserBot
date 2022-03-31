package com.snowbud56.command;

/*
* Created by snowbud56 on April 16, 2018
* Do not change or use this code without permission
*/

import com.snowbud56.KaiserBot;
import com.snowbud56.config.Config;
import com.snowbud56.general.ShutdownCommand;
import com.snowbud56.moderation.commands.ClearCommand;
import com.snowbud56.moderation.commands.SayCommand;
import com.snowbud56.rssfeed.commands.*;
import com.snowbud56.utils.managers.LogManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CommandManager extends ListenerAdapter {

    private static HashMap<String, Command> commands;
    private static List<Command> commandList;

    public CommandManager() {
        commands = new HashMap<>();
        commandList = new ArrayList<>();
        addCommands();
    }

    @Override
    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();
        MessageChannel channel = event.getChannel();
        if (message.startsWith(Config.getInstance().getString("commandPrefix"))) {
            String commandName = message.substring(1);
            String[] args = null;
            if (commandName.contains(" ")) {
                commandName = commandName.split(" ")[0];
                args = message.substring(message.indexOf(' ') + 1).split(" ");
            }
            Command command = commands.get(commandName.toLowerCase());
            if (command != null) {
                if (args == null) args = new String[]{};
                event.getMessage().delete().queue();
                LogManager.logConsole("Command executed: " + event.getAuthor().getName() + " (ID:" + event.getAuthor().getId() + ") executed " + Config.getInstance().getString("commandPrefix") + commandName.toLowerCase() + " " + Arrays.toString(args).replace("[", "").replace("]", "").replace(",", ""), false, true);
                runCommand(command, commandName, event.getMember(), event.getAuthor(), channel, event.getMessage(), args);
            }
        }
    }

    private void runCommand(Command command, String commandName, Member member, User user, MessageChannel channel, Message message, String[] args) {
        try {
            command.setAliasUsed(commandName.toLowerCase());

            command.setGuild(KaiserBot.getJDA().getGuildById(Config.getInstance().getString("guildID")));
            command.execute(member, channel, args);
        } catch (Exception e) {
            LogManager.logConsole("Something went wrong!", true, true);
            e.printStackTrace();
        }
    }

    public static List<Command> getCommandList() {
        return commandList;
    }

    private void addCommands() {
        addCommand(new ShutdownCommand());
        addCommand(new TestCommand());
        addCommand(new InfoCommand());
        addCommand(new SilenceCommand());
        addCommand(new ReloadCommand());
        addCommand(new ClearCommand());
        addCommand(new ToggleCommand());
        addCommand(new SayCommand());
    }

    private void addCommand(Command command) {
        for (String alias : command.getAliases()) {
            commands.put(alias.toLowerCase(), command);
            System.out.println("Initialized command: " + Config.getInstance().getString("commandPrefix") + alias);
        }
        commandList.add(command);
    }
}
