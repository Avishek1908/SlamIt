package com.example.bhargavbv.ureon.UploadVideo;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.bhargavbv.ureon.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import static com.example.bhargavbv.ureon.BuildConfig.DEBUG;

public class VideoActivity extends AppCompatActivity {
    VideoView videoView;
    Button button,videoupload;
    private static final int PICK_VIDEO = 100;
    Uri videoUri;
    DatabaseReference dref;
    Bitmap bmThumbnail;
    public String filepath,UID;
    StorageReference sref;
    private FirebaseAuth mAuth;


    private static final String TAG = "VideoActivity";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.video_upload_menu,menu);
        return  true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        videoView=(VideoView)findViewById(R.id.videoView);
        button=(Button)findViewById(R.id.button);
        videoupload =(Button)findViewById(R.id.upploadvideo);

        mAuth = FirebaseAuth.getInstance();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });

        videoupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captions();
            }
        });
        }
        private void openGallery(){
            Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.INTERNAL_CONTENT_URI);
            gallery.setType("video/*");
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

            //ImageView iv = (ImageView )findViewById(R.id.imageView);
            /*ContentResolver crThumb = getContentResolver();
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, requestCode, MediaStore.Video.Thumbnails.MICRO_KIND, options);
            iv.setImageBitmap(curThumb);*/

            filepath = getRealPathFromURI(videoUri);
            Log.i(TAG,filepath);
            bmThumbnail = ThumbnailUtils.createVideoThumbnail(filepath, MediaStore.Video.Thumbnails.MINI_KIND);
            //iv.setImageBitmap(bmThumbnail);
            Log.i(TAG,videoUri.toString());


        }
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }


    private void captions(){

        Intent intent = new Intent(VideoActivity.this,CaptionActivity.class);
        intent.putExtra("imageFilePath",filepath);
        intent.putExtra("videoFilePath",videoUri.toString());
        //Log.i(TAG,getIntent().getStringExtra("videoFilePath").toString());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.camera:
        }
        return super.onOptionsItemSelected(item);
    }
}




