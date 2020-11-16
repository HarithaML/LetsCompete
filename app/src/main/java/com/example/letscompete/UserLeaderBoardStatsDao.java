package com.example.letscompete;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserLeaderBoardStatsDao {
    @Query("SELECT * FROM userleaderboardstats")
    List<UserLeaderBoardStats> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(UserLeaderBoardStats... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllList(List<UserLeaderBoardStats> users);

    @Delete
    void delete(UserLeaderBoardStats user);

    @Query("Delete from userleaderboardstats")
    void deleteAll();
}


