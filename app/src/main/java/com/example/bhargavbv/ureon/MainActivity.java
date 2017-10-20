package com.example.bhargavbv.ureon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bhargavbv.ureon.UploadVideo.VideoActivity;
import com.example.bhargavbv.ureon.UserProfile.ProfileActivity;
import com.example.bhargavbv.ureon.models.ProfileInfo;
import com.example.bhargavbv.ureon.models.UserPosts;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView name;
    private ImageView photo;

    private DatabaseReference ref;

    private RecyclerView recyclerView;
    //adapter object
    private RecyclerView.Adapter adapter;
    //modelclass
    private List<UserPosts> users;

    private ProgressDialog pdialog;

    private GoogleApiClient mGoogleApiClient;
    private Button signout;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;


    CircleMenu circleMenu;

    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circleMenu = (CircleMenu) findViewById(R.id.circle_menu);

        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.mipmap.add, R.mipmap.remove);
        circleMenu.addSubMenu(Color.parseColor("#258CFF"), R.mipmap.profile)
                .addSubMenu(Color.parseColor("#30A400"), R.mipmap.upload)
                .addSubMenu(Color.parseColor("#30A400"), R.mipmap.points)
                .addSubMenu(Color.parseColor("#FF4B32"), R.mipmap.challenge)
                .addSubMenu(Color.parseColor("#8A39FF"), R.mipmap.settings)
                .addSubMenu(Color.parseColor("#FF6A00"), R.mipmap.settings)
                .addSubMenu(Color.parseColor("#FF6A00"), R.mipmap.discover);


        circleMenu.setOnMenuSelectedListener(new OnMenuSelectedListener() {

                                                 @Override
                                                 public void onMenuSelected(int index) {
                                                     switch (index) {
                                                         case 0:
                                                             startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                                                             break;
                                                         case 1:
                                                             startActivity(new Intent(MainActivity.this, VideoActivity.class));
                                                             break;
                                                         case 2:
                                                             Toast.makeText(MainActivity.this, "challenge button Clciked", Toast.LENGTH_SHORT).show();
                                                             //startActivity(new Intent(MainActivity.this, RecordVideo.class));
                                                             break;
                                                         case 5:
                                                             Toast.makeText(MainActivity.this, "Setting button Clcked", Toast.LENGTH_SHORT).show();
                                                             startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                                                             break;
                                                         case 3:
                                                             Toast.makeText(MainActivity.this, "Notify button Clcked", Toast.LENGTH_SHORT).show();
                                                             //startActivity(new Intent(MainActivity.this, RecordVideo.class));
                                                             break;

                                                     }
                                                 }
                                             }

        );

        circleMenu.setOnMenuStatusChangeListener(new OnMenuStatusChangeListener() {

                                                     @Override
                                                     public void onMenuOpened() {

                                                     }

                                                     @Override
                                                     public void onMenuClosed() {

                                                     }
                                                 }
        );


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
        };

        recyclerView = (RecyclerView) findViewById(R.id.recview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ref = FirebaseDatabase.getInstance().getReference();

        users = new ArrayList<>();

        user = mAuth.getCurrentUser();
        assert  user!=null;
        String uid = user.getUid();

        pdialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pdialog.setMessage("Please Wait...");
        pdialog.show();
        ref.child("users").child(uid).child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot Postsnapshot : dataSnapshot.getChildren())
                {
                    UserPosts model = Postsnapshot.getValue(UserPosts.class);
                    users.add(model);
                    Log.i(TAG,"Heloooooooo"+Postsnapshot.toString());

                    adapter.notifyDataSetChanged();
                }
                pdialog.dismiss();
                Log.i(TAG,"Heloooooooo"+users.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                pdialog.dismiss();
            }
        });

        adapter = new MainRecycleAdapter(MainActivity.this, users);
        recyclerView.setAdapter(adapter);


    }



    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void onBackPressed() {
        if (circleMenu.isOpened())
            circleMenu.closeMenu();
        else
            finish();

    }

}
