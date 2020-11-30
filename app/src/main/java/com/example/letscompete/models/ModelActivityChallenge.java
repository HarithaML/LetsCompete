package com.example.letscompete.models;

public class ModelActivityChallenge {
    String challengeTitle,  userId,  userName, counter;

    public ModelActivityChallenge() {
    }

    public ModelActivityChallenge(String challengeTitle, String userId, String userName, String counter) {
        this.challengeTitle = challengeTitle;
        this.userId = userId;
        this.userName = userName;
        this.counter = counter;
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

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }
}
