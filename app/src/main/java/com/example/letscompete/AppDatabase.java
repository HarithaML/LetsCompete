package com.example.letscompete;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserLeaderBoardStats.class, UserLeaderBoardChallenges.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{
    private static AppDatabase sInstance;
    public abstract UserLeaderBoardStatsDao userDao();
    public abstract  UserLeaderBoardChallengesDao leaderDao();

    public static AppDatabase getInstance(Context context)
    {
        if(sInstance ==null)
        {
            //please replace this later
            sInstance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "user-leaderboard").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }

        return sInstance;
    }

    public static void destroyInstance()
    {
        sInstance = null;
    }
}

