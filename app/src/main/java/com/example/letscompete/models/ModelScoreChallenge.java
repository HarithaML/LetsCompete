package com.example.letscompete.models;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class ModelScoreChallenge extends ModelChallengeGeneric{

    private String userUid;
    private String score;

    public ModelScoreChallenge() {
    }

    public ModelScoreChallenge( String userName, String userUid, String challengeTitle,String score) {
        super(userName, challengeTitle);
        this.userUid = userUid;
        this.score = score;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("challengeTitle", challengeTitle);
        result.put("userUid", userUid);
        result.put("userName", userName);
        result.put("score", score);

        return result;
    }
}
