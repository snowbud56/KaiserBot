package com.snowbud56.musicplayer.command;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.snowbud56.command.CommandBase;
import com.snowbud56.musicplayer.GuildMusicManager;
import com.snowbud56.musicplayer.PlayerManager;
import com.snowbud56.utils.TimeUnit;
import com.snowbud56.utils.TimeUtil;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class QueueCommand extends CommandBase {

    public QueueCommand() {
        super("queue");
    }

    @Override
    public void execute(SlashCommandEvent event) {
        GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);
        BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;

        AudioTrack trackPlaying = musicManager.audioPlayer.getPlayingTrack();

        if (queue.isEmpty()) {
            if (trackPlaying != null && !musicManager.audioPlayer.isPaused()) {
                AudioTrackInfo currentlyPlayingInfo = trackPlaying.getInfo();
                reply(event, false,"The queue is empty, but the current playing track is `" + currentlyPlayingInfo.title + "` by `" + currentlyPlayingInfo.author + "`");
            } else {
                reply(event, false,"The queue is currently empty and there is nothing playing.");
            }
            return;
        }

        boolean clear;
        if (event.getOption("clear") == null) {
            clear = false;
        } else {
            clear = event.getOption("clear").getAsBoolean();
        }

        if (clear) {
            musicManager.scheduler.queue.clear();
            reply(event, false,"Cleared queue, but will continue playing this song.");
            return;
        }

        int trackCount = Math.min(queue.size(), 20);
        List<AudioTrack> trackList = new ArrayList<>(queue);
        StringBuilder stringBuilder = new StringBuilder("**Current Queue**\n");

        AudioTrackInfo currentlyPlayingInfo = trackPlaying.getInfo();
        stringBuilder.append("NOW PLAYING `")
                .append(currentlyPlayingInfo.title)
                .append("` by `")
                .append(currentlyPlayingInfo.author).append("`\n");


        for (int i = 0; i < trackCount; i++) {
            AudioTrack audioTrack = trackList.get(i);
            if (stringBuilder.length() + getMessageFormatted(i, audioTrack).length() >= 1975)  {
                trackCount = i;
                break;
            }
            stringBuilder.append(getMessageFormatted(i, audioTrack));
        }

        if (trackList.size() > trackCount) {
            stringBuilder.append("And ")
                    .append(trackList.size() - trackCount)
                    .append(" more...");
        }

        reply(event, false, stringBuilder.toString());
    }

    private String getMessageFormatted(int i, AudioTrack audioTrack) {
        AudioTrackInfo info = audioTrack.getInfo();

        return "#" + (i + 1) + " `" + info.title + "` by `" + info.author + "` [`" + TimeUtil.getDuration(TimeUnit.FIT, audioTrack.getDuration()) + "`]\n";
    }

    @Override
    public CommandData getCommandData() {
        return commandData.addOption(OptionType.BOOLEAN, "clear", "Whether or not the queue should be cleared.");
    }

    @Override
    protected String getDescription() {
        return "Shows or clears the current queue for the audio player.";
    }
}
