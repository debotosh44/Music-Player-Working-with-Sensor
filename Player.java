package com.example.a1405231.project3;


import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import java.io.File;
import java.util.ArrayList;

public class Player extends AppCompatActivity implements View.OnClickListener,SensorEventListener {
    static MediaPlayer mp;
    ArrayList<File> mySongs;
    SeekBar sb;
    Button btPlay,btFF,btFB,btPrev,btNxt;
    int position;
    Uri u;
    Thread th;
    SensorManager sm;
    Sensor sensor2;
    float x,y,z;
    int a,b,c;
    // ToggleButton tb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        sb= (SeekBar) findViewById(R.id.seekBar);
        btPlay = (Button) findViewById(R.id.btplay);
        btFF = (Button) findViewById(R.id.btFF);
        btFB = (Button) findViewById(R.id.btFB);
        btPrev = (Button) findViewById(R.id.btPrev);
        btNxt = (Button) findViewById(R.id.btNxt);
        //btSensor=(Button)findViewById(R.id.btSensor);
        // tb=(ToggleButton)findViewById(R.id.toggleButton);

        sm=(SensorManager)getSystemService(SENSOR_SERVICE);
        // sensor1=sm.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        sensor2=sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        // sm.registerListener(this,sensor1,sm.SENSOR_DELAY_NORMAL);
        sm.registerListener(this,sensor2,sm.SENSOR_DELAY_NORMAL);
        //sensor=sm.getDefaultSensor(Sensor.TYPE_ORIENTATION);

        btPlay.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btFB.setOnClickListener(this);
        btPrev.setOnClickListener(this);
        btNxt.setOnClickListener(this);

        th=new Thread(){
            @Override
            public void run() {
                int totalDuration = mp.getDuration();
                int currentPosition = 0;
                // sb.setMax(totalDuration);
                while (currentPosition<totalDuration){
                    try{
                        sleep(500);
                        currentPosition=mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                //super.run();
            }
        };


        if(mp!=null){
            mp.stop();
            mp.release();
        }

        Intent i = getIntent();
        Bundle b =i.getExtras();
        mySongs = (ArrayList)b.getParcelableArrayList("songlist");
        position = b.getInt("pos",0);

        //URI defn

        u = Uri.parse(mySongs.get(position).toString());
        mp=MediaPlayer.create(getApplicationContext(), u);
        mp.start();
        sb.setMax(mp.getDuration());
        th.start();

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent event) {           // to get the value of sensor
        float f[]=event.values;
        x=f[0];//azimuth
        y=f[1];//pitch
        z=f[2];//roll
        //s=s+"x="+Float.toString(x)+"\n"+"y="+Float.toString(y)+"\n"+"z="+Float.toString(z);
        a=(int)x;
        b=(int)y;
        c=(int)z;
        if(x<-9){
            mp.stop();
            mp.release();
            position=(position+1)%mySongs.size();
            u = Uri.parse(mySongs.get(position).toString());
            mp=MediaPlayer.create(getApplicationContext(), u);
            mp.start();
            sb.setMax(mp.getDuration());
        }
        if(x>9){
            mp.stop();
            mp.release();
            position=((position-1)<0)?mySongs.size()-1:position-1;
            u = Uri.parse(mySongs.get(position).toString());
            mp=MediaPlayer.create(getApplicationContext(),u);
            mp.start();
            sb.setMax(mp.getDuration());
        }
        if(z>9){
            btPlay.setText("||");
            mp.start();
        }
        if(z<-8){
            btPlay.setText("(>)");
            mp.pause();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id) {
            case R.id.btplay:
                if (mp.isPlaying()) {
                    btPlay.setText("(>)");
                    mp.pause();
                } else {
                    btPlay.setText("||");
                    mp.start();
                }

                break;
            case R.id.btFF:
                mp.seekTo(mp.getCurrentPosition()+5000);
                break;
            case R.id.btFB:
                mp.seekTo(mp.getCurrentPosition()-5000);
                break;
            case R.id.btNxt:
                mp.pause();
                // mp.release();
                position=(position+1)%mySongs.size();
                u = Uri.parse(mySongs.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(), u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;
            case R.id.btPrev:
                mp.pause();
                // mp.release();
                position=((position-1)<0)?mySongs.size()-1:position-1;
                u = Uri.parse(mySongs.get(position).toString());
                mp=MediaPlayer.create(getApplicationContext(),u);
                mp.start();
                sb.setMax(mp.getDuration());
                break;

        }

    }

}
/*Intent i=new Intent(MainActivity.this,song.class);
                PendingIntent pi= PendingIntent.getService(MainActivity.this, (int) System.currentTimeMillis(), i, 0);
                Notification.Builder nb=new Notification.Builder(MainActivity.this);
                nb.setSmallIcon(R.mipmap.ic_launcher);
              //  nb.setTicker("email recieved");
                nb.setContentInfo("");
                nb.setContentTitle("Now playing");
                nb.setContentIntent(pi);
                nb.setContentText(mySong.get(position).getName().toString());
                nb.setAutoCancel(true);
                // nb.setStyle(style1);

                Intent j=new Intent();
                i.setAction("playsongs");
                PendingIntent pi2=PendingIntent.getService(MainActivity.this,(int)System.currentTimeMillis(),j,0);
                Intent k=new Intent();
                k.setAction("prevsongs");
                PendingIntent pi3=PendingIntent.getService(MainActivity.this,(int)System.currentTimeMillis(),k,0);
                Intent l=new Intent();
                l.setAction("nextsongs");
                PendingIntent pi4=PendingIntent.getService(MainActivity.this,(int)System.currentTimeMillis(),l,0);
                nb.addAction(android.R.drawable.ic_lock_power_off,"prev",pi3);
                nb.addAction(android.R.drawable.ic_lock_power_off,"play",pi2);
                nb.addAction(android.R.drawable.ic_lock_power_off,"next",pi4);
                Notification n=nb.build();
                nm.notify((int)System.currentTimeMillis(),n);
                }*/
