package com.example.bhargavbv.ureon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.hitomi.cmlibrary.OnMenuStatusChangeListener;


public class ProfileActivity extends AppCompatActivity {

    CircleMenu circleMenu;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        circleMenu = (CircleMenu) findViewById(R.id.circle_menu);
        img = (ImageView)findViewById(R.id.button);

        circleMenu.setMainMenu(Color.parseColor("#CDCDCD"), R.mipmap.add, R.mipmap.remove);
        circleMenu.addSubMenu(Color.parseColor("#258CFF"), R.mipmap.profile)
                .addSubMenu(Color.parseColor("#30A400"), R.mipmap.upload)
                .addSubMenu(Color.parseColor("#30A400"), R.mipmap.points)
                .addSubMenu(Color.parseColor("#FF4B32"), R.mipmap.challenge)
                .addSubMenu(Color.parseColor("#8A39FF"), R.mipmap.settings)
                .addSubMenu(Color.parseColor("#FF6A00"), R.mipmap.notification)
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
                                                         case 3:
                                                             Toast.makeText(ProfileActivity.this, "Setting button Clcked", Toast.LENGTH_SHORT).show();
                                                             //startActivity(new Intent(Profile.this, ThankYouActivity.class));
                                                             break;
                                                         case 4:
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

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,EditProfileActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (circleMenu.isOpened())
            circleMenu.closeMenu();
        else
            finish();
    }
    }
