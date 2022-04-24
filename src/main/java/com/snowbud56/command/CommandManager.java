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
import com.snowbud56.rssfeed.FeedManager;
import com.snowbud56.rssfeed.commands.*;
import com.snowbud56.rssfeed.feeds.Feed;
import com.snowbud56.utils.managers.LogManager;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
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
    public void onSlashCommand(SlashCommandEvent event) {
        String cmdName = event.getCommandString().substring(1) + " placeholder";
        System.out.println("command received: " + cmdName.substring(0, cmdName.indexOf(" ")));
        Command command = commands.get(cmdName.substring(0, cmdName.indexOf(" ")));
        if (command != null) {
            System.out.println("command processed: " + command.getCommandName());
            command.setGuild(event.getGuild());
            command.setChannel(event.getChannel());
            LogManager.logConsole("Command executed: " + event.getMember().getUser().getName() + " (ID:" + event.getMember().getUser().getId() + ") executed command \"" + event.getCommandString() + "\"", false, true);
            runCommand(command, event);
        }
        else {
            event.reply("Something happened and I wasn't able to find that command. Please contact snowbud56 to fix it.").queue();
        }
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        String[] id = event.getComponentId().split(":");
        String authorID = id[0];
        String feedID = id[1];
        String commandName = id[2];

        if (!authorID.equals(event.getMember().getId()))
            return;

        Feed feed = FeedManager.getFeed(Integer.parseInt(feedID));
        commands.get(commandName.toLowerCase()).onButtonPress(feed, event);
    }

    //    @Override
//    public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
//        String message = event.getMessage().getContentRaw();
//        MessageChannel channel = event.getChannel();
//        if (message.startsWith(Config.getInstance().getString("commandPrefix"))) {
//            String commandName = message.substring(1);
//            String[] args = null;
//            if (commandName.contains(" ")) {
//                commandName = commandName.split(" ")[0];
//                args = message.substring(message.indexOf(' ') + 1).split(" ");
//            }
//            Command command = commands.get(commandName.toLowerCase());
//            if (command != null) {
//                if (args == null) args = new String[]{};
//                event.getMessage().delete().queue();
//                LogManager.logConsole("Command executed: " + event.getAuthor().getName() + " (ID:" + event.getAuthor().getId() + ") executed " + Config.getInstance().getString("commandPrefix") + commandName.toLowerCase() + " " + Arrays.toString(args).replace("[", "").replace("]", "").replace(",", ""), false, true);
//                runCommand(command, event);
//            }
//        }
//    }

    private void runCommand(Command command, SlashCommandEvent event) {
        try {
            command.setGuild(event.getGuild());
            command.execute(event);
        } catch (Exception e) {
            LogManager.logConsole("Something went wrong!", true, true);
            e.printStackTrace();
        }
    }

    public static List<Command> getCommandList() {
        return commandList;
    }

    private void addCommands() {
        addCommand(new ShutdownCommand(),
                new UpdateCommand(),
                new InfoCommand(),
                new SilenceCommand(),
                new ReloadCommand(),
                new ClearCommand(),
                new ToggleCommand(),
                new SayCommand());
    }

    private void addCommand(Command... cmds) {
        CommandListUpdateAction slashCommands = KaiserBot.getJDA().updateCommands();
        for (Command command : cmds) {
            System.out.println("Initialized command: /" + command.getCommandName().toLowerCase());

            commands.put(command.getCommandName().toLowerCase(), command);

            slashCommands.addCommands(command.getCommandData());
            commandList.add(command);
        }

        slashCommands.queue();
    }
}
