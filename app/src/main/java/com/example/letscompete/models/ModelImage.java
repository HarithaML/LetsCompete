package com.example.letscompete.models;

public class ModelImage {
    private String imageUrl,userName,userUid,challengeTitle;

    public ModelImage() {
    }

    public ModelImage(String imageUrl, String userName, String userUid, String challengeTitle) {
        this.imageUrl = imageUrl;
        this.userName = userName;
        this.userUid = userUid;
        this.challengeTitle = challengeTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getChallengeTitle() {
        return challengeTitle;
    }

    public void setChallengeTitle(String challengeTitle) {
        this.challengeTitle = challengeTitle;
    }
}
