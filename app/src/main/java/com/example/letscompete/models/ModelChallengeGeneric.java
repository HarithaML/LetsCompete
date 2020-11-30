package com.example.letscompete.models;

public class ModelChallengeGeneric {
    String challengeTitle,  userId,  userName;
    public ModelChallengeGeneric() {
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

    public ModelChallengeGeneric(String challengeTitle, String userId, String userName) {
        this.challengeTitle = challengeTitle;
        this.userId = userId;
        this.userName = userName;
    }
}
