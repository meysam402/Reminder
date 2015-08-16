package com.meysam.reminder;

/**
 * Created by Meysam on 8/13/2015.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.FileInputStream;
import java.util.Calendar;

public class AlarmService extends Service {

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public void onCreate(){
        super.onCreate();
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
    }
    public void onDestroy(){
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(todayIsRequested()) {
            playSound();
            notifyTheUser();
            Toast.makeText(this, "It\'s Workout Time!", Toast.LENGTH_LONG).show();
        }
        return Service.START_NOT_STICKY;
    }


    private void notifyTheUser(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setContentIntent(PendingIntent.getActivity(this, 0, new Intent(), 0));
        mBuilder.setSmallIcon(R.mipmap.exercise_notification);
        mBuilder.setContentTitle("زمان ورزش");
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder.setAutoCancel(true);
        // notificationID allows you to update the notification later on.
        mNotificationManager.notify(111, mBuilder.build());
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

    private boolean todayIsRequested(){
        try {
            FileInputStream fin = openFileInput("days.txt");
            int c;
            String content="";
            while ((c = fin.read()) != -1) {
                content = content + Character.toString((char) c);
            }
            fin.close();
            if(content.charAt(getTodaysIndex()) == '1') return true;
        }
        catch(Exception ex){

        }
        return false;
    }

    private int getTodaysIndex(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.SATURDAY:
                day = 0;
                break;
            case Calendar.SUNDAY:
                day = 1;
                break;
            case Calendar.MONDAY:
                day = 2;
                break;
            case Calendar.TUESDAY:
                day = 3;
                break;
            case Calendar.WEDNESDAY:
                day = 4;
                break;
            case Calendar.THURSDAY:
                day = 5;
                break;
            case Calendar.FRIDAY:
                day = 6;
                break;
        }
        return day;
    }
}
