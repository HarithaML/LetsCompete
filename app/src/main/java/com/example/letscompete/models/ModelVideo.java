package com.example.letscompete.models;

public class ModelVideo {
    String challengeTitle,uuid,videoUrl;

    public ModelVideo() {
    }

    public ModelVideo(String challengeTitle, String uuid, String videoUrl) {
        this.challengeTitle = challengeTitle;
        this.uuid = uuid;
        this.videoUrl = videoUrl;
    }

    public String getChallengeTitle() {
        return challengeTitle;
    }

    public void setChallengeTitle(String challengeTitle) {
        this.challengeTitle = challengeTitle;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
