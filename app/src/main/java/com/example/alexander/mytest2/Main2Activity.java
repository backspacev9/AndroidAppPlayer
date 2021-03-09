package com.example.alexander.mytest2;


import android.content.Intent;

import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;

import android.text.TextWatcher;

import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ListView;


import java.io.File;

import java.util.ArrayList;



public class Main2Activity extends AppCompatActivity{

float x1,x2,y1,y2;

    Button btnExit,btnDetector;
    Button temp;
    ArrayList<String> arrayList;
    MediaPlayer mediaPlayer;
    ListView listView;
    String[] items;
    EditText searchEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        listView = (ListView) findViewById(R.id.listView);
        searchEdit = (EditText) findViewById(R.id.editText);


       final  ArrayList<File> mySong = findSongs(Environment.getExternalStorageDirectory());
        MediaMetadataRetriever  md = new MediaMetadataRetriever();

        items = new String[mySong.size()];
        for(int i=0; i<mySong.size();i++){
            md.setDataSource(mySong.get(i).getPath());
            String Title = md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            String  Artist = md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);

            items[i] = Title+"\n ->"+Artist;//mySong.get(i).getName().toString();
        }

        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(),R.layout.layout_songs,R.id.textView4,items);
        listView.setAdapter(adp);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(getApplicationContext(), Player.class).putExtra("pos", position).putExtra("songlist", mySong));


            }
        });

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                ArrayList<String> templist = new ArrayList<>();
                for(String temp:items){
                    if(temp.toLowerCase().contains(s.toString().toLowerCase())){
                        templist.add(temp);
                    }
                }
                ArrayAdapter<String> adp = new ArrayAdapter<>(getApplicationContext(),R.layout.layout_songs,R.id.textView4,templist);
                listView.setAdapter(adp);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    public boolean onTouchEvent(MotionEvent touchEvent){
        switch (touchEvent.getAction()){
            case MotionEvent.ACTION_DOWN:
                x1=touchEvent.getX();
                y1=touchEvent.getY();
                break;
            case MotionEvent.ACTION_UP:
                x2=touchEvent.getX();
                y2=touchEvent.getY();
                if(x1>x2){
                    Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                    startActivity(intent);

                    finish();
                   overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }
                break;
        }

        return  false;
    }


    public ArrayList<File> findSongs(File root){

        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for (File singleFile : files){

            if(singleFile.isDirectory() && !singleFile.isHidden()){
                al.addAll(findSongs(singleFile));
            }
            else{
                if (singleFile.getName().endsWith(".mp3")){
                    al.add(singleFile);
                }
            }
        }

        return  al;
    }


}







