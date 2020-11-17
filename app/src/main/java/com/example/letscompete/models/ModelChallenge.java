package com.example.letscompete.models;

public class ModelChallenge {
    String challengeDescription, challengeDuration, challengeTitle, challengeType, imageName, imageURL, startdate;

    public ModelChallenge() {
    }

    public ModelChallenge(String challengeDescription, String challengeDuration, String challengeTitle, String challengeType, String imageName, String imageURL, String startdate){
        this.challengeDescription = challengeDescription;
        this.challengeDuration = challengeDuration;
        this.challengeTitle = challengeTitle;
        this.challengeType = challengeType;
        this.imageName = imageName;
        this.imageURL = imageURL;
        this.startdate = startdate;
    }

    public String getChallengeDescription() {
        return challengeDescription;
    }

    public void setChallengeDescription(String challengeDescription) {
        this.challengeDescription = challengeDescription;
    }

    public String getChallengeDuration() {
        return challengeDuration;
    }

    public void setChallengeDuration(String challengeDuration) {
        this.challengeDuration = challengeDuration;
    }

    public String getChallengeTitle() {
        return challengeTitle;
    }

    public void setChallengeTitle(String challengeTitle) {
        this.challengeTitle = challengeTitle;
    }

    public String getChallengeType() {
        return challengeType;
    }

    public void setChallengeType(String challengeType) {
        this.challengeType = challengeType;
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

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

}
