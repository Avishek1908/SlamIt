package com.example.bhargavbv.ureon;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.bhargavbv.ureon.models.UserPosts;

import java.net.URI;
import java.util.List;

/**
 * Created by BhargavBV on 21-10-2017.
 */

public class MainRecycleAdapter extends RecyclerView.Adapter<MainRecycleAdapter.ViewHolder> {


    private Context c;
    private List<UserPosts> models;


    public MainRecycleAdapter(Context c, List<UserPosts> models) {
        this.c = c;
        this.models = models;
    }

    @Override
    public MainRecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_video, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);

        /*v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context c = v.getContext();
                Intent intent = new Intent(c, QuestionsViewActivity.class);
                c.startActivity(intent);
            }

            });*/
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MainRecycleAdapter.ViewHolder holder, int position) {

        UserPosts model = models.get(position);

        holder.videoView.setVideoPath(model.getVideoUrl());
        holder.videoView.start();

    }

    @Override
    public int getItemCount() {
        return models.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public VideoView videoView;

        public ViewHolder(View itemView) {
            super(itemView);

            videoView = itemView.findViewById(R.id.videoview);
        }
    }
}
