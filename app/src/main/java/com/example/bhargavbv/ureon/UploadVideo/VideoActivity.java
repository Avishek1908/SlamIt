package com.example.bhargavbv.ureon.UploadVideo;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import com.example.bhargavbv.ureon.R;

public class VideoActivity extends AppCompatActivity {
    VideoView videoView;
    Button button;
    private static final int PICK_VIDEO = 100;
    Uri videoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView=(VideoView)findViewById(R.id.videoView);
        button=(Button)findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });


        }
        private void openGallery(){
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI);
            //gallery.setType("Video/*");
             startActivityForResult(gallery, PICK_VIDEO);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK &&  requestCode == PICK_VIDEO){
            videoUri = data.getData ();
            videoView.setVideoURI(videoUri);
            videoView.requestFocus();
            videoView.start();
        }
    }
}
