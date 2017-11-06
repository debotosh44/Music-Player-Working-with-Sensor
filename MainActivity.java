package com.example.a1405231.project3;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        lv= (ListView) findViewById(R.id.lvPlaylist);

        final ArrayList<File> mySong= findSongs(Environment.getExternalStorageDirectory());
        items = new String[mySong.size()];
        for(int i=0;i<mySong.size();i++){
            //toast(mySongs.get(i).getName().toString());
            items[i]=mySong.get(i).getName().toString().replace("mp3","").replace(".wav","");

        }
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(),R.layout.layout_song,R.id.textView,items);
        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),Player.class).putExtra("pos", position).putExtra("songlist", mySong));

            }

        });

    }

    public ArrayList<File> findSongs(File root){
        ArrayList<File> al=new ArrayList<File>();

        File[] files = root.listFiles();
        for(File singleFile : files) {
            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                al.addAll(findSongs(singleFile));
            }
            else{
                if(singleFile.getName().endsWith(".mp3")||singleFile.getName().endsWith(".wav")){
                    al.add(singleFile);
                }
            }
        }
        return al;
    }
    public void toast(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
/*Intent i=new Intent(MainActivity.this,Player.class);
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
