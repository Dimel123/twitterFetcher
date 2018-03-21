package app.my_group.com.myapplication.database;

import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.twitter.sdk.android.core.models.Identifiable;
import com.twitter.sdk.android.core.models.UserEntities;

import java.io.Serializable;
import java.util.List;


public class MyUser {

    private String name;
    private String profileBackgroundImageUrl;


    public MyUser() {
    }

    public MyUser(String profileBackgroundImageUrl, String name) {
        this.name = name;
        this.profileBackgroundImageUrl = profileBackgroundImageUrl;
    }

    public String getName() {
        return name;
    }

    public String getProfileBackgroundImageUrl() {
        return profileBackgroundImageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileBackgroundImageUrl(String profileBackgroundImageUrl) {
        this.profileBackgroundImageUrl = profileBackgroundImageUrl;
    }
}