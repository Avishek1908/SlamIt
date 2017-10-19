package com.example.bhargavbv.ureon.UploadVideo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.bhargavbv.ureon.R;

import java.net.MalformedURLException;
import java.net.URL;

public class ViewVideoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_video);

        TextView tv = (TextView)findViewById(R.id.tvcaption);
        VideoView videoView = (VideoView)findViewById(R.id.videoView);
        String tl;
        String cap;
            tl = getIntent().getStringExtra("VideoUrl");
            cap = getIntent().getStringExtra("captiontext");
            tv.setText(cap);
            videoView.setVideoPath(tl);
            videoView.start();
    }
}
