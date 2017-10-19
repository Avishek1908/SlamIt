package com.example.bhargavbv.ureon;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;
import android.media.AudioManager;


public class RecordVideo extends AppCompatActivity {
    VideoView prev;
    Button recVid;
    static final int REQUEST_VIDEO_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_video);
        recVid = (Button)findViewById(R.id.recvid);
        prev =(VideoView)findViewById(R.id.prev);
        recVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureVideo(v);
                prev.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {

                        mediaPlayer.setLooping(true);

                        prev.start();
                    }
                });
            }

        });


    }

    public void captureVideo(View view){

        Intent cameraIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if(cameraIntent.resolveActivity(getPackageManager())!= null)
            startActivityForResult(cameraIntent,REQUEST_VIDEO_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK){
            Uri uri = intent.getData();
            prev.setVideoURI(uri);
        }
    }

}
