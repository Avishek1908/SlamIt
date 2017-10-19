package com.example.bhargavbv.ureon.UploadVideo;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ThumbnailUtils;
import android.media.audiofx.BassBoost;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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
    private ImageButton imageButton;
    private TextView textView;
    private LocationManager locationManager;
    private LocationListener locationListener;


    DatabaseReference dref;
    StorageReference sref;
    FirebaseUser user;
    private FirebaseAuth mAuth;
    Uri videoUri;
    public String filepath, UID;
    private String pushkey;
    Bitmap bmThumbnail;

    EditText etcaptions;
    ImageView iv;

    Boolean cap = false;

    private static final String TAG = "CaptionsActivity";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 10 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureImageButton();
                return;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caption);
        imageButton = (ImageButton) findViewById(R.id.ImgButton);
        textView = (TextView) findViewById(R.id.textView);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                textView.append("\n " + location.getLatitude() + "" + location.getLongitude());

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);

            }
        };
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET

                }, 10);
                return;
            } else {
                configureImageButton();
            }
        }


        mAuth = FirebaseAuth.getInstance();
        sref = FirebaseStorage.getInstance().getReference();
        dref = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        assert user != null;
        UID = user.getUid();
        pushkey = dref.push().getKey().toString();


        filepath = getIntent().getStringExtra("imageFilePath");
        videoUri = Uri.parse(getIntent().getStringExtra("videoFilePath"));
        //Log.i(TAG,getIntent().getStringExtra("videoFilePath").toString());

        bmThumbnail = ThumbnailUtils.createVideoThumbnail(filepath, MediaStore.Video.Thumbnails.MINI_KIND);

        Button slamit = (Button) findViewById(R.id.slamit);
        etcaptions = (EditText) findViewById(R.id.etcaptions);
        iv = (ImageView) findViewById(R.id.ivthumbnail);


        slamit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etcaptions.getText().toString() != null)
                    dref.child("users").child(UID).child("posts").child(pushkey).child("caption").setValue(etcaptions.getText().toString());
                new AsyncCaller().execute();
            }
        });

        iv.setImageBitmap(bmThumbnail);
    }

    private void configureImageButton() {
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(CaptionActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CaptionActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates("gps", 5000, 100, locationListener);

            }
        });

    }


    public class AsyncCaller extends AsyncTask<Void, Integer,Void >
    {
        ProgressDialog pdLoading = new ProgressDialog(CaptionActivity.this);

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
            //startActivity(new Intent(VideoActivity.this,CaptionActivity.class));
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            pdLoading.setProgress(10);
        }


    }
}
