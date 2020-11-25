package com.example.letscompete;

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

    @Ignore
    @ColumnInfo(name = "picture")
    Bitmap picture;


    public Bitmap getPicture() {
        return picture;
    }

    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    @NonNull
    public String getChallengename() {
        return challengename;
    }

    public void setChallengename(@NonNull String challengename) {
        this.challengename = challengename;
    }
}
