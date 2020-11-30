package com.example.letscompete.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.letscompete.entities.UserLeaderBoardStats;

import java.util.List;

@Dao
public interface UserLeaderBoardStatsDao {
    @Query("SELECT * FROM userleaderboardstats Order by rank")
    List<UserLeaderBoardStats> getAll();

    @Query("SELECT * FROM userleaderboardstats Order by rank Asc")
    List<UserLeaderBoardStats> getAllAsc();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(UserLeaderBoardStats... users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllList(List<UserLeaderBoardStats> users);

    @Delete
    void delete(UserLeaderBoardStats user);

    @Query("Delete from userleaderboardstats")
    void deleteAll();

    @Query("Select * from userleaderboardstats where username = :user")
    List<UserLeaderBoardStats> getUser(String user);

    @Query("Update userleaderboardstats Set picture = :pic where username = :user")
    void updatePicture(String pic, String user);
}


