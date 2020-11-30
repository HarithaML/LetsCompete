package com.example.letscompete.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.letscompete.entities.UserLeaderBoardChallenges;

import java.util.List;

@Dao
public interface UserLeaderBoardChallengesDao {
    @Query("SELECT * FROM userLeaderBoardChallenges")
    List<UserLeaderBoardChallenges> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(UserLeaderBoardChallenges... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllList(List<UserLeaderBoardChallenges> users);

    @Delete
    void delete(UserLeaderBoardChallenges user);

    @Query("Delete from UserLeaderBoardChallenges")
    void deleteAll();
}


