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
                new AsyncCaller().execute();
                //uploadtask();
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

            ImageView iv = (ImageView )findViewById(R.id.imageView);
            /*ContentResolver crThumb = getContentResolver();
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, requestCode, MediaStore.Video.Thumbnails.MICRO_KIND, options);
            iv.setImageBitmap(curThumb);*/

            filepath = getRealPathFromURI(videoUri);
            Log.i(TAG,filepath);
            bmThumbnail = ThumbnailUtils.createVideoThumbnail(filepath, MediaStore.Video.Thumbnails.MINI_KIND);
            iv.setImageBitmap(bmThumbnail);
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

    private void uploadtask(){

    }

    public class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        ProgressDialog pdLoading = new ProgressDialog(VideoActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here


            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imagethumb = baos.toByteArray();

            sref = FirebaseStorage.getInstance().getReference();
            dref = FirebaseDatabase.getInstance().getReference();

            FirebaseUser user = mAuth.getCurrentUser();

            assert user!=null;
            UID = user.getUid();

            /*sref.child("videos").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).child(System.currentTimeMillis()+filepath.substring(filepath.lastIndexOf(".")+1,filepath.length())).putFile(videoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl1 = taskSnapshot.getDownloadUrl();
                            //dref.child("users").setValue(downloadUrl1);
                            Log.i(TAG,downloadUrl1.toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG,e.toString());
                        }
                    });*/

            sref.child("Thumbnails").child(System.currentTimeMillis() + ".jpg").putBytes(imagethumb)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl2 = taskSnapshot.getDownloadUrl();
                            dref.child("users").child(UID).child("posts").child(dref.push().getKey().toString()).child("imgUrl").setValue(downloadUrl2.toString());
                            //dref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("posts").child(dref.push().getKey()).child("imgUrl").setValue(downloadUrl);
                            Log.i(TAG,downloadUrl2.toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Log.i(TAG,exception.toString());
                        }
                    });
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //this method will be running on UI thread

            pdLoading.dismiss();
        }

    }
}




