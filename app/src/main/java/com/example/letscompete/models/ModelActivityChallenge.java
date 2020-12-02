package com.example.letscompete.models;

public class ModelActivityChallenge extends ModelChallengeGeneric{
    String userId, counter;

    public ModelActivityChallenge() {
    }

    public ModelActivityChallenge(String challengeTitle, String userId, String userName, String counter) {
        super(challengeTitle,userName);
        this.userId = userId;
        this.counter = counter;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }
}
