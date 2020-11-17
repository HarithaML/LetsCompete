package com.example.letscompete.activities;

import java.util.Date;

public class Challenge {

    private String ChallengeName;
    private String ChallengeTitle;
    private String ChallengeDuration;
    private String ChallengeDescription;
    private String StartDate;
    public String imageName;
    public String imageURL;
    private String UserID;

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Challenge(String name, String url,String startdate) {
        this.imageName = name;
        this.imageURL = url;
    }
    public Challenge(String name, String startdate) {
        this.imageName = name;
        this.Startdate = startdate;
    }

    public String getImageName() {
        return imageName;
    }
    public String getImageURL() {
        return imageURL;
    }
    public String getChallengeType() {
        return ChallengeType;
    }

    public void setChallengeType(String challengeType) {
        ChallengeType = challengeType;
    }

    private String ChallengeType;

    public String getStartdate()
    {
        return Startdate;
    }

    public void setStartdate(String startdate)
    {
        Startdate = startdate;
    }

    public Challenge(String startdate)
    {
        Startdate = startdate;
    }

    private String Startdate;





    public String getChallengeName() {
        return ChallengeName;
    }

    public void setChallengeName(String challengeName) {
        ChallengeName = challengeName;
    }

    public String getChallengeTitle() {
        return ChallengeTitle;
    }

    public void setChallengeTitle(String challengeTitle) {
        ChallengeTitle = challengeTitle;
    }

    public String getChallengeDuration() {
        return ChallengeDuration;
    }

    public void setChallengeDuration(String challengeDuration) {
        ChallengeDuration = challengeDuration;
    }

    public String getChallengeDescription() {
        return ChallengeDescription;
    }

    public void setChallengeDescription(String challengeDescription) {
        ChallengeDescription = challengeDescription;
    }

    public Challenge() {
    }
}
