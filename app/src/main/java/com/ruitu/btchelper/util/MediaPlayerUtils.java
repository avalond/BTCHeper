package com.ruitu.btchelper.util;

import java.io.IOException;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

public class MediaPlayerUtils {

    public static void cancelMediaplayer(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                mediaPlayer.stop();
            }
//            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static void resetMediaPlayer(MediaPlayer mediaPlayer, Uri uri,
            Context context) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, uri);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.setLooping(false);
                mediaPlayer.prepare();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    public static void backToBeginMediaPlayer(MediaPlayer mediaPlayer) {
        mediaPlayer.seekTo(0);
    }

    public static void startMediaPlayer(MediaPlayer mediaPlayer, Uri uri,
            Context context) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(context, uri);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mediaPlayer.setLooping(false);
                mediaPlayer.prepare();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }

    }
}
