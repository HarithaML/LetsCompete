package com.example.letscompete;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserLeaderBoardStats.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{
    private static AppDatabase sInstance;
    public abstract UserLeaderBoardStatsDao userDao();

    public static AppDatabase getInstance(Context context)
    {
        if(sInstance ==null)
        {
            //please replace this later
            sInstance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "user-leaderboard").allowMainThreadQueries().build();
        }

        return sInstance;
    }

    public static void destroyInstance()
    {
        sInstance = null;
    }
}

