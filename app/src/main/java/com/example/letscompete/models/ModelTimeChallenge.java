package com.example.letscompete.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ModelTimeChallenge extends ModelChallengeGeneric{
    String time, userId;
    public ModelTimeChallenge() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }

    public ModelTimeChallenge(String challengeTitle, String userId, String userName, String time) {
        super(challengeTitle,userName);
        this.userId = userId;
        this.time = time;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("challengeTitle", challengeTitle);
        result.put("userId", userId);
        result.put("userName", userName);
        result.put("time", time);

        return result;
    }


}
