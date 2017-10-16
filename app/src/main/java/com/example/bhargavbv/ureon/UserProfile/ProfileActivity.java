package com.example.bhargavbv.ureon.UserProfile;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bhargavbv.ureon.MainActivity;
import com.example.bhargavbv.ureon.R;
import com.example.bhargavbv.ureon.SettingsActivity;
import com.example.bhargavbv.ureon.models.ProfileInfo;
import com.example.bhargavbv.ureon.models.UserPosts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ProfileActivity extends AppCompatActivity {

    CircleMenu circleMenu;
    ImageView img;
    TextView name;
    private GridView gv;
    private GridViewAdapter gvadapter;

    private FirebaseAuth mAuth;
    private DatabaseReference ref;
    private com.google.firebase.auth.FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;

    String name1;
    String photo1;

    private ArrayList<UserPosts> userposts;
    private static final String TAG = "ProfileActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        circleMenu = (CircleMenu) findViewById(R.id.circle_menu);
        img = (ImageView)findViewById(R.id.button);
        name = (TextView)findViewById(R.id.name);
        gv = (GridView)findViewById(R.id.gv);



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
                                                             Toast.makeText(ProfileActivity.this, "profile is Clicked", Toast.LENGTH_SHORT).show();
                                                             break;
                                                         case 1:
                                                             Toast.makeText(ProfileActivity.this, "upload button Clicked", Toast.LENGTH_SHORT).show();
                                                             break;
                                                         case 2:
                                                             Toast.makeText(ProfileActivity.this, "challenge button Clciked", Toast.LENGTH_SHORT).show();
                                                             break;
                                                         case 4:
                                                             Toast.makeText(ProfileActivity.this, "Setting button Clcked", Toast.LENGTH_SHORT).show();
                                                             startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
                                                             break;
                                                         case 3:
                                                             Toast.makeText(ProfileActivity.this, "notify button Clicked", Toast.LENGTH_SHORT).show();
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

        ref = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();

        assert  user!=null;
        String uid = user.getUid();
        ref.child("users").child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ProfileInfo info = dataSnapshot.getValue(ProfileInfo.class);
                name1 = info.getName();
                photo1 = info.getPhoto();
                Log.i(TAG,info.getName()+info.getPhoto());
                img.setImageBitmap(getBitmapFromURL(photo1));
                name.setText(name1);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        userposts = new ArrayList<>();

        ref.child("users").child(uid).child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userPostSnapshot : dataSnapshot.getChildren())
                {
                    Log.i(TAG,userPostSnapshot.toString());
                    UserPosts userpost = userPostSnapshot.getValue(UserPosts.class);
                    userposts.add(userpost);

                }

                int gridWidth = getResources().getDisplayMetrics().widthPixels;
                int gridHeigth = getResources().getDisplayMetrics().heightPixels;
                int imageWidth = gridWidth/3;
                gvadapter = new GridViewAdapter(getApplicationContext(),userposts,imageWidth,gridHeigth);
                gv.setColumnWidth(imageWidth);
                gv.setAdapter(gvadapter);
                gvadapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                    Log.i(TAG,databaseError.getDetails());
            }
        });




        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,EditProfileActivity.class));
            }
        });

    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.i(TAG,src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            //Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
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
