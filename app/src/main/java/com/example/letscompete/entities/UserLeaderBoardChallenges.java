package com.example.letscompete.entities;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity
public class UserLeaderBoardChallenges {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "challengename")
    public String challengename;

    @ColumnInfo(name = "type")
    public String type;
    @ColumnInfo(name = "duration")
    public String duration;

    @ColumnInfo(name = "picture")
    String picture;


    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @NonNull
    public String getChallengename() {
        return challengename;
    }

    public void setChallengename(@NonNull String challengename) {
        this.challengename = challengename;
    }

}
