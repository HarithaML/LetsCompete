package com.example.letscompete.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ModelTimeChallenge extends ModelChallengeGeneric{
    String time;
    public ModelTimeChallenge() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ModelTimeChallenge(String challengeTitle, String userId, String userName, String time) {
        super(challengeTitle,userId,userName);
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
