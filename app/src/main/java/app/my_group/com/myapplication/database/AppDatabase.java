package app.my_group.com.myapplication.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;


@Database(entities = {MyTweet.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TweetDao tweetDao();
}
