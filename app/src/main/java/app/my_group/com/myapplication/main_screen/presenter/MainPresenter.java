package app.my_group.com.myapplication.main_screen.presenter;

import android.arch.persistence.room.Room;
import android.util.Log;

import com.arellomobile.mvp.InjectViewState;
import com.arellomobile.mvp.MvpPresenter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;


import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import app.my_group.com.MyApplication;
import app.my_group.com.myapplication.R;
import app.my_group.com.myapplication.common.managers.NetworkManager;
import app.my_group.com.myapplication.database.AppDatabase;
import app.my_group.com.myapplication.database.MyTweet;
import app.my_group.com.myapplication.database.MyUser;
import app.my_group.com.myapplication.database.TweetDao;
import app.my_group.com.myapplication.main_screen.view.MainView;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

@InjectViewState
public class MainPresenter extends MvpPresenter<MainView> {

    AppDatabase db;
    SearchService searchService;
    TweetDao tweetDao;
    List<Long> idColumns;
    boolean isFirstDoubleElement;

    public MainPresenter() {
        db = Room.databaseBuilder(MyApplication.getAppComponent().getApp(),
                AppDatabase.class, "database").build();
        tweetDao = db.tweetDao();
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        searchService = twitterApiClient.getSearchService();
    }

    public void loadData(String tag, Long maxId) {
        getViewState().showProgress();
        Flowable<List<MyTweet>> flowable = NetworkManager.isOnline() ? loadFromNet(tag, maxId) : loadFromDb(maxId);

        flowable
                .onErrorResumeNext(new Function<Throwable, Publisher<? extends List<MyTweet>>>() {
                    @Override
                    public Publisher<? extends List<MyTweet>> apply(Throwable throwable) throws Exception {
                        return loadFromDb(null);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MyTweet>>() {
                    @Override
                    public void accept(List<MyTweet> myTweets) throws Exception {
                        getViewState().hideProgress();
                        getViewState().showData(myTweets);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getViewState().hideProgress();
                        getViewState().showError(throwable.getMessage());
                    }
                });

    }

    public void loadMore(String tag, Long maxId) {
        Flowable<List<MyTweet>> flowable = NetworkManager.isOnline() ? loadFromNet(tag, maxId) : loadFromDb(maxId);
        flowable
                .onErrorResumeNext(new Function<Throwable, Publisher<? extends List<MyTweet>>>() {
                    @Override
                    public Publisher<? extends List<MyTweet>> apply(Throwable throwable) throws Exception {
                        return loadFromDb(null);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<MyTweet>>() {
                    @Override
                    public void accept(List<MyTweet> myTweets) throws Exception {
                        getViewState().showData(myTweets);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getViewState().showError(throwable.getMessage());
                    }
                });
    }

    private Flowable<List<MyTweet>> loadFromDb(Long maxId) {
        return Flowable.fromCallable(new Callable<List<MyTweet>>() {
            @Override
            public List<MyTweet> call() throws Exception {
                if (maxId == null) {
                    return tweetDao.getFirstTweets();
                } else {
                    return tweetDao.getTweets(maxId);
                }

            }
        });
    }

    public Flowable<List<MyTweet>> loadFromNet(String tag, Long maxId) {
        return Flowable.create(new FlowableOnSubscribe<List<Tweet>>() {
            @Override
            public void subscribe(FlowableEmitter<List<Tweet>> e) throws Exception {

                idColumns = tweetDao.getIdColumn();

                Response<Search> execute = searchService.tweets(tag, null, null, null,
                        null, 60, null, null, maxId, null).execute();
                if (execute.body() != null && execute.isSuccessful()) {
                    isFirstDoubleElement = maxId != null;
                    List<Tweet> tweets = execute.body().tweets;
                    e.onNext(tweets);
                    e.onComplete();
                } else {
                    if (execute.errorBody() != null) {
                        e.onError(new Throwable(execute.errorBody().string()));
                    } else {
                        e.onError(new Throwable(MyApplication.getAppComponent().getApp().getString(R.string.connection_error)));
                    }
                }
            }
        }, BackpressureStrategy.BUFFER)
                .flatMap(new Function<List<Tweet>, Publisher<Tweet>>() {
                    @Override
                    public Publisher<Tweet> apply(List<Tweet> tweets) throws Exception {
                        return Flowable.fromIterable(tweets);
                    }
                })
                .filter(new Predicate<Tweet>() {
                    @Override
                    public boolean test(Tweet tweet) throws Exception {
                        if(isFirstDoubleElement){
                            isFirstDoubleElement = false;
                            return false;
                        } else {
                            return true;
                        }
                    }
                })
                .map(new Function<Tweet, MyTweet>() {
                    @Override
                    public MyTweet apply(Tweet tweet) throws Exception {
                        return new MyTweet(tweet.getId(), tweet.text, new MyUser(tweet.user.profileImageUrl, tweet.user.name));
                    }
                })
                .doOnNext(new Consumer<MyTweet>() {
                    @Override
                    public void accept(MyTweet tweet) throws Exception {
                        if (!idColumns.contains(tweet.getId())) {
                            tweetDao.insert(tweet);
                        } else {
                            tweetDao.update(tweet);
                        }
                    }
                })
                .toList()
                .toFlowable();
    }

}
