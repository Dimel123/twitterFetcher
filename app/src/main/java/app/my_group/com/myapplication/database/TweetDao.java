package app.my_group.com.myapplication.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TweetDao {

    @Query("SELECT * FROM MyTweet WHERE id < :id ORDER BY id DESC LIMIT 60")
    List<MyTweet> getTweets(Long id);

    @Query("SELECT * FROM MyTweet ORDER BY id DESC LIMIT 60")
    List<MyTweet> getFirstTweets();

    @Query("SELECT id FROM MyTweet")
    List<Long> getIdColumn();

    @Insert
    void insert(MyTweet tweet);

    @Update
    void update(MyTweet tweet);

}
