package app.my_group.com.myapplication.database;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

/**
 * A MyTweet is the basic atomic building block of all things Twitter. Tweets, also known more
 * generically as "status updates." Tweets can be embedded, replied to, favorited, unfavorited and
 * deleted.
 */
@Entity
public class MyTweet {

    @PrimaryKey
    @SerializedName("id")
    private long id;
    private String text;

    @Embedded
    private MyUser user;

    public MyTweet() {
    }

    public MyTweet(long id, String text, MyUser user) {
        this.id = id;
        this.text = text;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public MyUser getUser() {
        return user;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }
}
