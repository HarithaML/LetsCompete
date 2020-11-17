package com.example.letscompete.models;

public class ModelChallenge {
    private String challengeType;
    private String challengeTitle;
    private String challengeDuration;
    private String challengeDescription;
    private String startdate;
    public String imageName;
    public String imageURL;
    private String userID;


    public ModelChallenge() {
    }

    public ModelChallenge(String imageName, String imageURL) {
        this.imageName = imageName;
        this.imageURL = imageURL;
    }

    public ModelChallenge(String challengeType, String challengeTitle, String challengeDuration, String challengeDescription, String startdate, String imageName, String imageURL, String userID) {
        this.challengeType = challengeType;
        this.challengeTitle = challengeTitle;
        this.challengeDuration = challengeDuration;
        this.challengeDescription = challengeDescription;
        this.startdate = startdate;
        this.imageName = imageName;
        this.imageURL = imageURL;
        this.userID = userID;
    }

    public String getChallengeType() {
        return challengeType;
    }

    public void setChallengeType(String challengeType) {
        this.challengeType = challengeType;
    }

    public String getChallengeTitle() {
        return challengeTitle;
    }

    public void setChallengeTitle(String challengeTitle) {
        this.challengeTitle = challengeTitle;
    }

    public String getChallengeDuration() {
        return challengeDuration;
    }

    public void setChallengeDuration(String challengeDuration) {
        this.challengeDuration = challengeDuration;
    }

    public String getChallengeDescription() {
        return challengeDescription;
    }

    public void setChallengeDescription(String challengeDescription) {
        this.challengeDescription = challengeDescription;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
