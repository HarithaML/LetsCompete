package com.example.letscompete.models;

public class ModelTimeChallenge {
    String challengeTitle,  userId,  userName, time;
    public ModelTimeChallenge() {
    }

    public String getChallengeTitle() {
        return challengeTitle;
    }

    public void setChallengeTitle(String challengeTitle) {
        this.challengeTitle = challengeTitle;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ModelTimeChallenge(String challengeTitle, String userId, String userName, String time) {
        this.challengeTitle = challengeTitle;
        this.userId = userId;
        this.userName = userName;
        this.time = time;
    }
}
