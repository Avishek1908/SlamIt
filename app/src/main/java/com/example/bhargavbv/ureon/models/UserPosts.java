package com.example.bhargavbv.ureon.models;

/**
 * Created by BhargavBV on 09-10-2017.
 */

public class UserPosts {

    String caption;
    String imgUrl;


    String videoUrl;

    public UserPosts(){

    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

}
