package com.example.letscompete.models;

public class ModelChallengeGeneric {
    String challengeTitle,  userName;
    public ModelChallengeGeneric() {
    }

    public String getChallengeTitle() {
        return challengeTitle;
    }

    public void setChallengeTitle(String challengeTitle) {
        this.challengeTitle = challengeTitle;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ModelChallengeGeneric(String challengeTitle, String userName) {
        this.challengeTitle = challengeTitle;
        this.userName = userName;
    }
}
