package com.example.letscompete.activities;

import android.net.Uri;

public class Participants {

    private String ChallengeTitle;

    public String getChallengeTitle() {
        return ChallengeTitle;
    }

    public void setChallengeTitle(String challengeTitle) {
        ChallengeTitle = challengeTitle;
    }

    public String getProgress() {
        return Progress;
    }

    public void setProgress(String progress) {
        Progress = progress;
    }

    public String getRank() {
        return Rank;
    }

    public void setRank(String rank) {
        Rank = rank;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserUid() {
        return UserUid;
    }

    public void setUserUid(String userUid) {
        UserUid = userUid;
    }

    private String Progress;
    private String Rank;
    private String Status;



    public void setUserImage(Uri userImage) {
        UserImage = userImage;
    }

    private Uri UserImage;
    private String UserName;
    private String UserUid;

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    private String Role;
    public Participants()
    {

    }
}
