package com.snowbud56.musicplayer;

import net.dv8tion.jda.api.audio.hooks.ConnectionListener;
import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

public class ConnectionLogger implements ConnectionListener {
    @Override
    public void onPing(long l) {

    }

    @Override
    public void onStatusChange(@NotNull ConnectionStatus connectionStatus) {
        System.out.println(connectionStatus);
    }

    @Override
    public void onUserSpeaking(@NotNull User user, boolean b) {

    }
}
