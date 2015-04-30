package com.example.root.main.alarmandmusic;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.RelativeLayout;

import com.example.root.musicNav.MusicService;
import com.example.root.scroll.*;

import static com.example.root.scroll.ScrollBaseAdapter.*;
import com.example.root.musicNav.MusicService.*;
import com.example.root.musicNav.*;


import java.util.ArrayList;


public class MainActivity extends Activity {


    // scroll list items
    private ArrayList<Item> itemsData;
    // scroll layout
    private ScrollLayout scrollLayout;

    // music backend part
    private MusicUtils musicUtils;
    // music nav view part
    private MusicNavLayout musicNavLayout;

    // service staff start
    private MusicService musicService;
    private Intent serviceIntent;
    private ServiceConnection musicServiceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        itemsData = ScrollUtils.getItems(NUM_OF_ITEM);
        scrollLayout = new ScrollLayout(this, itemsData);
        // music backend part of music nav
        musicUtils = new MusicUtils(this);

        if (!musicUtils.getSongsFromDevice()) {
            // work wrong
        } else {
            // work right
        }

        // music service init start
        musicServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MusicBinder musicBinder = (MusicBinder)service;
                musicService = musicBinder.getMusicService();
                musicService.setSongList(musicUtils.getSongList());
                musicNavLayout.setMusicService(musicService);
                musicNavLayout.addListeners();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };

        // music service init end

        RelativeLayout mainActivity = (RelativeLayout)findViewById(R.id.activity_main);
        musicNavLayout = new MusicNavLayout(this, mainActivity);
    }
    // music backend start**************
    @Override
    protected void onStart() {
        super.onStart();
        if (serviceIntent == null) {
            serviceIntent = new Intent(this, MusicService.class);
            bindService(serviceIntent, musicServiceConnection, Context.BIND_AUTO_CREATE);
            startService(serviceIntent);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(musicServiceConnection);

    }
    // music backend stop*****************
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