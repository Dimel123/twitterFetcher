package app.my_group.com.myapplication.main_screen.view;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import app.my_group.com.myapplication.database.AppDatabase;
import app.my_group.com.myapplication.database.MyTweet;
import app.my_group.com.myapplication.database.MyUser;
import app.my_group.com.myapplication.database.TweetDao;

import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
public class DatabaseTest {

    private static final String TEST_UPDATE = "test44";
    TweetDao tweetDao;

    @Before
    public void setUp() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        AppDatabase db = Room.inMemoryDatabaseBuilder(context, AppDatabase.class).build();
        tweetDao = db.tweetDao();
        tweetDao.insert(new MyTweet(5, "test5", new MyUser()));
        tweetDao.insert(new MyTweet(1, "test1", new MyUser()));
        tweetDao.insert(new MyTweet(4, "test4", new MyUser()));
        tweetDao.update(new MyTweet(4, TEST_UPDATE, new MyUser()));
    }

    @Test
    public void testGetFromDbOrderBy() throws Exception {
        List<MyTweet> tweets = tweetDao.getFirstTweets();
        assertTrue(tweets.get(0).getId() > tweets.get(1).getId());
        assertTrue(tweets.get(1).getId() > tweets.get(2).getId());
    }

    @Test
    public void testUpdate() throws Exception {
        List<MyTweet> tweets = tweetDao.getFirstTweets();
        assertTrue(tweets.stream().anyMatch(item -> TEST_UPDATE.equals(item.getText())));
    }

}