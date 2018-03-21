package app.my_group.com.myapplication.main_screen.view;

import com.arellomobile.mvp.MvpView;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import app.my_group.com.myapplication.database.MyTweet;

public interface MainView extends MvpView {

    void showData(List<MyTweet> list);

    void showProgress();

    void hideProgress();

    void showError(String text);

}
