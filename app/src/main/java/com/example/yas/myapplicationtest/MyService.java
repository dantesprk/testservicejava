package com.example.yas.myapplicationtest;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MyService extends Service {
    private Timer timer;
    private TimerTask timerTask;
    public int counter=0;
    public MyService instance_;

    private Handler mHandler = new Handler();

    public MyService() {

        instance_ = this;
    }

    public void startTimer() {
        timer = new Timer();
        initializeTimerTask();
        timer.schedule(timerTask, 0, 60000); //
    }

    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                mHandler.post(new Runnable() {

                    @Override
                    public void run() {

                        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        File file = new File(path, "tmp.txt");

                        try
                        {
                            file.createNewFile();
                            FileOutputStream fOut = new FileOutputStream(file,true);
                            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(" dd:MMMM:yyyy HH:mm:ss a");
                            final String strDate = simpleDateFormat.format(calendar.getTime());

                            myOutWriter.append(Integer.toString(counter) + strDate + "\n");
                            myOutWriter.close();

                            fOut.flush();
                            fOut.close();

                            NotificationCompat.Builder builder =
                                    new NotificationCompat.Builder(instance_, "channel-01")
                                            .setSmallIcon(R.mipmap.ic_launcher)
                                            .setContentTitle("Title")
                                            .setContentText("Notification text").setOnlyAlertOnce(true);

                            startForeground (45, builder.build());
                        }
                        catch (IOException e)
                        {
                            Log.e("Exception", "File write failed: " + e.toString());
                        }

                    }

                });



            }
        };
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(this,"Created", Toast.LENGTH_SHORT).show();

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "channel-01")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Title")
                        .setContentText("Notification text").setOnlyAlertOnce(true);

        startForeground (45, builder.build());
        startTimer();
    }



    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
