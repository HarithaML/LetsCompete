package com.example.letscompete.models;

public class ModelScoreChallenge {

    private String userName;
    private String userUid;
    private String challengeTitle;
    private String score;

    public ModelScoreChallenge() {
    }

    public ModelScoreChallenge( String userName, String userUid, String challengeTitle,String score) {

        this.userName = userName;
        this.userUid = userUid;
        this.challengeTitle = challengeTitle;
        this.score = score;
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

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
