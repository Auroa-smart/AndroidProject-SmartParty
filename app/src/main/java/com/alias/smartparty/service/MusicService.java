package com.alias.smartparty.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.alias.smartparty.R;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    public static final String ACTION_PLAY = "PLAY";
    public static final String ACTION_PAUSE = "PAUSE";

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case ACTION_PLAY:
                    playMusic();
                    break;
                case ACTION_PAUSE:
                    pauseMusic();
                    break;
                default:
                    break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopSelf();
    }

    private void playMusic() {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }

    private void pauseMusic() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}