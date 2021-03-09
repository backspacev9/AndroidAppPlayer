package com.example.alexander.mytest2;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import com.example.alexander.mytest2.DeviceListFragment;

public class Player extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG ="tag";
    ImageButton btnStopPlay;
    ImageButton btnPrev;
    ImageButton btnNext;
    ImageButton btnListSong;
    static MediaPlayer mp;
    ArrayList<File> mySongs;
    SeekBar sb;
   Uri uri;
    Thread updateSeekBar;
    TextView textViewTitle,textViewArtist,textViewTimer,textViewTimerTotal;
    String Title;
    String Artist;
    ImageView imageView,imageView2;
    byte[] art;
    Bitmap songImage;
    int position;


    WifiP2pManager.Channel channel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);


        btnStopPlay = (ImageButton)findViewById(R.id.btnStopPlay);
        btnPrev = (ImageButton)findViewById(R.id.btnPrev);
        btnNext = (ImageButton)findViewById(R.id.btnNext);
        btnListSong = (ImageButton)findViewById(R.id.btnListSong);
        textViewArtist = (TextView) findViewById(R.id.textViewArtist);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewTimer = (TextView) findViewById(R.id.textViewTimer);
        textViewTimerTotal = (TextView) findViewById(R.id.textViewTimerTotal);
        imageView =(ImageView) findViewById(R.id.imageView);
        imageView2 =(ImageView) findViewById(R.id.imageView2);




        btnStopPlay.setOnClickListener(this);
        btnPrev.setOnClickListener(this);
        btnNext.setOnClickListener(this);


        sb = (SeekBar) findViewById(R.id.seekBar);
        updateSeekBar = new Thread(){
            @Override
            public void run() {

                int totalDuration = mp.getDuration();
                int currentPosition = 0;

                while (mp!=null && currentPosition < totalDuration){
                    try{
                        Thread.sleep(200);
                        currentPosition = mp.getCurrentPosition();


                   } catch (InterruptedException e){
                       e.printStackTrace();
                   }
                    catch (Exception e){
                        return;
                    }
                    sb.setProgress(currentPosition);

                }

            }
        };

        if(mp!=null){
        mp.stop();
            mp.release();

        }


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        mySongs = (ArrayList) bundle.getParcelableArrayList("songlist");
        position = bundle.getInt("pos", 0);
        uri = Uri.parse(mySongs.get(position).toString());


        mp = MediaPlayer.create(getApplicationContext(), uri);
        getMusic();
        mp.start();
        sb.setMax(mp.getDuration());
        updateSeekBar.start();


        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                                textViewTimer.setText(getTimeString(mp.getCurrentPosition()));
                                textViewTimerTotal.setText(getTimeString(mp.getDuration()));
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();


        mp.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
            @Override
            public void onSeekComplete(MediaPlayer mp) {

            }
        });

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




        btnListSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Player.this, Main2Activity.class);
                startActivity(intent);


            }
        });

        repeatMusic();
        textViewTitle.setSelected(true);


    }




    public void changeWifiName(String newName) {

        WifiP2pDevice dev =new  WifiP2pDevice();
        String mydev =dev.deviceName;

        String deviceName ="/"+ newName+mydev;

        /*if (deviceName.length() > MAX_DEVICE_NAME_LENGTH) {
            deviceName = DEFAULT_DEVICE_NAME + mPort;
        }*/
        try {
            WifiP2pManager manager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
            WifiP2pManager.Channel channel = manager.initialize(this, getMainLooper(), null);
            Class[] paramTypes = new Class[3];
            paramTypes[0] = WifiP2pManager.Channel.class;
            paramTypes[1] = String.class;
            paramTypes[2] = WifiP2pManager.ActionListener.class;
            Method setDeviceName = manager.getClass().getMethod(
                    "setDeviceName", paramTypes);


            setDeviceName.setAccessible(true);


            Object arglist[] = new Object[3];
            arglist[0] = channel;
            arglist[1] = deviceName;
            arglist[2] = new WifiP2pManager.ActionListener() {

                @Override
                public void onSuccess() {
                    Log.d(TAG, "setDeviceName succeeded");
                }

                @Override
                public void onFailure(int reason) {
                    Log.d(TAG, "setDeviceName failed");
                }
            };


            setDeviceName.invoke(manager, arglist);

        } catch (NoSuchMethodException e) {
            Log.d(TAG, e.getMessage());
        } catch (IllegalAccessException e) {
            Log.d(TAG, e.getMessage());
        } catch (IllegalArgumentException e) {
            Log.d(TAG, e.getMessage());
        } catch (InvocationTargetException e) {
            Log.d(TAG, e.getMessage());
        }
    }


    private String getTimeString(long millis) {
        StringBuffer buf = new StringBuffer();

      //  int hours = (int) (millis / (1000 * 60 * 60));
        int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
        int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

        buf
               // .append(String.format("%02d", hours))
              //  .append(":")
                .append(String.format("%02d", minutes))
                .append(":")
                .append(String.format("%02d", seconds));

        return buf.toString();
    }

    @Override
    public void onClick(View v) {

        int id =v.getId();
        switch (id){
            case R.id.btnStopPlay:
               try {
                   if (mp.isPlaying()) {
                       btnStopPlay.setImageResource(R.drawable.btnplay);
                       mp.pause();
                   } else {
                       btnStopPlay.setImageResource(R.drawable.btnstop);
                       mp.start();

                   }
                   updateSeekBar.start();
               }catch (Exception e){}
                break;
            case R.id.btnNext:
                try {
                    mp.stop();
                    mp.release();
                    position = (position + 1) % mySongs.size();
                    uri = Uri.parse(mySongs.get(position).toString());
                    mp = MediaPlayer.create(getApplicationContext(), uri);
                    mp.start();
                    sb.setMax(mp.getDuration());
                    btnStopPlay.setImageResource(R.drawable.btnstop);
                    getMusic();
                    updateSeekBar.start();
                }catch (Exception e){}
                break;
            case R.id.btnPrev:
                try {
                    mp.stop();
                    mp.release();
                    position = (position - 1 < 0) ? mySongs.size() - 1 : position - 1;
                    uri = Uri.parse(mySongs.get(position).toString());
                    mp = MediaPlayer.create(getApplicationContext(), uri);
                    mp.start();
                    sb.setMax(mp.getDuration());
                    btnStopPlay.setImageResource(R.drawable.btnstop);
                    getMusic();
                    updateSeekBar.start();
                }catch (Exception e){}
                break;

        }
                repeatMusic();


    }
    public void repeatMusic() {

        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp1) {
try {

                mp.release();
                position = (position + 1) % mySongs.size();
                uri = Uri.parse(mySongs.get(position).toString());
                mp = MediaPlayer.create(getApplicationContext(), uri);
                mp.start();
                sb.setMax(mp.getDuration());
                btnStopPlay.setImageResource(R.drawable.btnstop);
                getMusic();
                mp.setOnCompletionListener(this);
                updateSeekBar.start();
                //   Toast.makeText(getApplicationContext(), "прошло", Toast.LENGTH_SHORT).show();
}catch (Exception e){}
            }
        });

    }


    public void getMusic() {
        MediaMetadataRetriever md = new MediaMetadataRetriever();
        md.setDataSource(uri.getPath());

        Title = md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        Artist = md.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        art = md.getEmbeddedPicture();
        if (art !=null) {
            songImage = BitmapFactory.decodeByteArray(art, 0, art.length);

        } else {
            songImage = null;
            songImage = BitmapFactory.decodeResource(getResources(), R.drawable.disc);

        }
        imageView.setImageBitmap(songImage);
        imageView2.setImageBitmap(songImage);
        textViewTitle.setText(Title);
        textViewArtist.setText(Artist);
      String ssid="Song: "+ Artist +"-"+ Title;

       // changeWifiName(ssid);

    }
}
