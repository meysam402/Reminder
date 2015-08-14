package com.meysam.reminder;

/**
 * Created by Meysam on 8/13/2015.
 */
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

public class AlarmService extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Toast.makeText(this, "It\'s Workout Time!", Toast.LENGTH_LONG).show();
        playSound();
        return Service.START_NOT_STICKY;
    }

    public void playSound(){
        MediaPlayer mp = new MediaPlayer();
        try {
            AssetFileDescriptor afd = getAssets().openFd("alarm.mp3");
            mp.setDataSource(afd.getFileDescriptor());
            mp.prepare();
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
