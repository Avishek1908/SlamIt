package com.example.bhargavbv.ureon.UploadVideo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

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

public class CaptionActivity extends AppCompatActivity {

    DatabaseReference dref;
    StorageReference sref;
    FirebaseUser user;
    private FirebaseAuth mAuth;
    Uri videoUri;
    public String filepath,UID;
    private String pushkey;
    Bitmap bmThumbnail;

    RadioButton sports,dance,music,quick,rap;

    EditText etcaptions;
    ImageView iv;
    ProgressDialog pdLoading;

    Boolean cap = false;

    private static final String TAG ="CaptionsActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption);

        mAuth = FirebaseAuth.getInstance();
        sref = FirebaseStorage.getInstance().getReference();
        dref = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        assert user!=null;
        UID = user.getUid();
        pushkey = dref.push().getKey().toString();

        sports = (RadioButton)findViewById(R.id.sports);
        music = (RadioButton)findViewById(R.id.music);
        dance = (RadioButton)findViewById(R.id.dance);
        quick = (RadioButton)findViewById(R.id.quick);
        rap = (RadioButton)findViewById(R.id.rap);

        filepath = getIntent().getStringExtra("imageFilePath");
        videoUri = Uri.parse(getIntent().getStringExtra("videoFilePath"));
        //Log.i(TAG,getIntent().getStringExtra("videoFilePath").toString());

        pdLoading = new ProgressDialog(CaptionActivity.this);

        bmThumbnail = ThumbnailUtils.createVideoThumbnail(filepath, MediaStore.Video.Thumbnails.MINI_KIND);

        Button slamit = (Button)findViewById(R.id.slamit);
        etcaptions = (EditText)findViewById(R.id.etcaptions);
        iv = (ImageView)findViewById(R.id.ivthumbnail);



        slamit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etcaptions.getText().toString()!=null)
                    dref.child("users").child(UID).child("posts").child(pushkey).child("caption").setValue(etcaptions.getText().toString());
                pdLoading.setMessage("Helloo");
                new AsyncCaller().execute();
            }
        });

        iv.setImageBitmap(bmThumbnail);
    }



    public class AsyncCaller extends AsyncTask<Void, Integer,Void >
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.show();
        }
        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here


            pdLoading.show();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmThumbnail.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imagethumb = baos.toByteArray();

            sref = FirebaseStorage.getInstance().getReference();
            dref = FirebaseDatabase.getInstance().getReference();



            sref.child("videos").child(FirebaseAuth.getInstance().getCurrentUser().getDisplayName()).child(System.currentTimeMillis()+filepath.substring(filepath.lastIndexOf(".")+1,filepath.length())).putFile(videoUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl1 = taskSnapshot.getDownloadUrl();
                            dref.child("users").child(UID).child("posts").child(pushkey).child("videoUrl").setValue(downloadUrl1.toString());
                            if(sports.isChecked())
                            {
                                dref.child("posts").child(pushkey).child("uid").setValue(UID);
                                dref.child("posts").child(pushkey).child("videoUrl").setValue(downloadUrl1.toString());
                                dref.child("category").child("sports").child(pushkey).child("uid").setValue(UID);
                                dref.child("category").child("sports").child(pushkey).child("videoUrl").setValue(downloadUrl1.toString());
                            }
                            if(rap.isChecked())
                            {
                                dref.child("posts").child(pushkey).child("uid").setValue(UID);
                                dref.child("posts").child(pushkey).child("videoUrl").setValue(downloadUrl1.toString());
                                dref.child("category").child("rap").child(pushkey).child("uid").setValue(UID);
                                dref.child("category").child("rap").child(pushkey).child("videoUrl").setValue(downloadUrl1.toString());
                            }
                            if(dance.isChecked())
                            {
                                dref.child("posts").child(pushkey).child("uid").setValue(UID);
                                dref.child("posts").child(pushkey).child("videoUrl").setValue(downloadUrl1.toString());
                                dref.child("category").child("dance").child(pushkey).child("uid").setValue(UID);
                                dref.child("category").child("dance").child(pushkey).child("videoUrl").setValue(downloadUrl1.toString());
                            }
                            if(music.isChecked())
                            {
                                dref.child("posts").child(pushkey).child("uid").setValue(UID);
                                dref.child("posts").child(pushkey).child("videoUrl").setValue(downloadUrl1.toString());
                                dref.child("category").child("music").child(pushkey).child("uid").setValue(UID);
                                dref.child("category").child("music").child(pushkey).child("videoUrl").setValue(downloadUrl1.toString());
                            }
                            if(quick.isChecked())
                            {
                                dref.child("posts").child(pushkey).child("uid").setValue(UID);
                                dref.child("posts").child(pushkey).child("videoUrl").setValue(downloadUrl1.toString());
                                dref.child("category").child("quick").child(pushkey).child("uid").setValue(UID);
                                dref.child("category").child("quick").child(pushkey).child("videoUrl").setValue(downloadUrl1.toString());
                            }
                            Log.i(TAG,downloadUrl1.toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG,e.toString());
                        }
                    });

            sref.child("Thumbnails").child(System.currentTimeMillis() + ".jpg").putBytes(imagethumb)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            Uri downloadUrl2 = taskSnapshot.getDownloadUrl();
                            dref.child("users").child(UID).child("posts").child(pushkey).child("imgUrl").setValue(downloadUrl2.toString());
                            //dref.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("posts").child(dref.push().getKey()).child("imgUrl").setValue(downloadUrl);
                            Log.i(TAG,downloadUrl2.toString());
                            if(sports.isChecked())
                            {
                                dref.child("posts").child(pushkey).child("uid").setValue(UID);
                                dref.child("posts").child(pushkey).child("imgUrl").setValue(downloadUrl2.toString());
                                dref.child("category").child("sports").child(pushkey).child("uid").setValue(UID);
                                dref.child("category").child("sports").child(pushkey).child("imgUrl").setValue(downloadUrl2.toString());
                            }
                            if(rap.isChecked())
                            {
                                dref.child("posts").child(pushkey).child("uid").setValue(UID);
                                dref.child("posts").child(pushkey).child("imgUrl").setValue(downloadUrl2.toString());
                                dref.child("category").child("rap").child(pushkey).child("uid").setValue(UID);
                                dref.child("category").child("rap").child(pushkey).child("imgUrl").setValue(downloadUrl2.toString());
                            }
                            if(dance.isChecked())
                            {
                                dref.child("posts").child(pushkey).child("uid").setValue(UID);
                                dref.child("posts").child(pushkey).child("imgUrl").setValue(downloadUrl2.toString());
                                dref.child("category").child("dance").child(pushkey).child("uid").setValue(UID);
                                dref.child("category").child("dance").child(pushkey).child("imgUrl").setValue(downloadUrl2.toString());
                            }
                            if(music.isChecked())
                            {
                                dref.child("posts").child(pushkey).child("uid").setValue(UID);
                                dref.child("posts").child(pushkey).child("imgUrl").setValue(downloadUrl2.toString());
                                dref.child("category").child("music").child(pushkey).child("uid").setValue(UID);
                                dref.child("category").child("music").child(pushkey).child("imgUrl").setValue(downloadUrl2.toString());
                            }
                            if(quick.isChecked())
                            {
                                dref.child("posts").child(pushkey).child("uid").setValue(UID);
                                dref.child("posts").child(pushkey).child("imgUrl").setValue(downloadUrl2.toString());
                                dref.child("category").child("quick").child(pushkey).child("uid").setValue(UID);
                                dref.child("category").child("quick").child(pushkey).child("imgUrl").setValue(downloadUrl2.toString());
                            }
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

            Toast.makeText(CaptionActivity.this,"Done",Toast.LENGTH_LONG).show();
            pdLoading.dismiss();
            //startActivity(new Intent(VideoActivity.this,CaptionActivity.class));
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            pdLoading.setProgress(10);
        }


    }
}
