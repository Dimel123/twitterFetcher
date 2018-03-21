package app.my_group.com;


import android.app.Application;
import android.util.Log;

import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import app.my_group.com.myapplication.dagger.component.AppComponent;
import app.my_group.com.myapplication.dagger.component.DaggerAppComponent;
import app.my_group.com.myapplication.dagger.modules.ApplicationModule;

public class MyApplication extends Application {

    public static final String CONSUMER_KEY = "mGzUg4f0KvkbbVDC9mSQcthaS";
    public static final String CONSUMER_SECRET = "schpLZD89gJ24ysSKQk0E2s7WqukE0TpZpEHef50idnAyUwNAc";
    private static AppComponent sAppComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        initComponent();
    }

    private void initComponent() {
        sAppComponent = DaggerAppComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();


        twitterInit();

    }

    private void twitterInit() {
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(CONSUMER_KEY, CONSUMER_SECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);

    }

    public static AppComponent getAppComponent() {
        return sAppComponent;
    }

}
