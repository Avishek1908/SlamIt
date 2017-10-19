package com.example.bhargavbv.ureon.UserProfile;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bhargavbv.ureon.R;
import com.example.bhargavbv.ureon.UploadVideo.ViewVideoActivity;
import com.example.bhargavbv.ureon.models.UserPosts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static android.support.constraint.R.id.parent;


/**
 * Created by BhargavBV on 09-10-2017.
 */

public class GridViewAdapter extends BaseAdapter {

    int ImageWidth,ImageHeigth;
    Context c;
    ArrayList<UserPosts> userposts;

    public GridViewAdapter(Context c, ArrayList<UserPosts> userposts, int ImageWidth,int ImageHeigth)
    {
        this.ImageHeigth = ImageHeigth;
        this.ImageWidth = ImageWidth;
        this.c = c;
        this.userposts = userposts;
    }
    @Override
    public int getCount() {
        return userposts.size();
    }

    @Override
    public Object getItem(int position) {
        return userposts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null)
        {
            convertView= LayoutInflater.from(c).inflate(R.layout.user_profile_grid_model,parent,false);
        }
        ImageView imgView = (ImageView)convertView.findViewById(R.id.imageView);
        //TextView nameTxt= (TextView) convertView.findViewById(R.id.tvcaption);
        final UserPosts s= (UserPosts) this.getItem(position);
        Picasso.with(c)
                .load(s.getImgUrl())
                .resize(ImageWidth,300 ) // here you resize your image to whatever width and height you like
                .into(imgView);

        //nameTxt.setText(s.getCaption());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //OPEN DETAIL
                Intent intent = new Intent(c,ViewVideoActivity.class);
                intent.putExtra("VideoUrl",s.getVideoUrl());
                intent.putExtra("captiontext",s.getCaption());
                Log.i("Hello",s.getVideoUrl());
                c.startActivity(intent);
                //Toast.makeText(c,s.getImgUrl(),Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }


}
