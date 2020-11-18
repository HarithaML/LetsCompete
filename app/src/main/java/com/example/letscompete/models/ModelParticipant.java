package com.example.letscompete.models;

public class ModelParticipant {
    /*role= Challenge giver/Challenge Taker
    *progress = ongoing/completed
    * rank = caluclate
    * */
    public String challengeTitle,userName,userImage,userUID,progress,role,rank,status;

    public ModelParticipant() {
    }



    public ModelParticipant(String challengeTitle, String userName, String userImage, String userUID, String progress, String role, String rank, String status) {
        this.challengeTitle = challengeTitle;
        this.userName = userName;
        this.userImage = userImage;
        this.userUID = userUID;
        this.progress = progress;
        this.role = role;
        this.rank = rank;
        this.status = status;
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

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
