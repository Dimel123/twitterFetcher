package app.my_group.com.myapplication.main_screen.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arellomobile.mvp.MvpAppCompatActivity;
import com.arellomobile.mvp.presenter.InjectPresenter;
import java.util.List;

import app.my_group.com.MyApplication;
import app.my_group.com.myapplication.R;
import app.my_group.com.myapplication.common.CustomLayoutManager;
import app.my_group.com.myapplication.database.MyTweet;
import app.my_group.com.myapplication.main_screen.presenter.MainPresenter;

public class MainActivity extends MvpAppCompatActivity implements MainView {

    public static final String MAIN_TAG = "fifa2018";
    @InjectPresenter
    MainPresenter mainPresenter;

    RecyclerView recyclerView;
    TweetAdapter adapter;
    ProgressBar progressBar;
    boolean isLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MyApplication.getAppComponent().inject(this);
        mainPresenter.loadData(MAIN_TAG, null);
        recyclerView = findViewById(R.id.list);
        progressBar = findViewById(R.id.progress);
        setupAdapter();
    }

    private void setupAdapter() {
        recyclerView.setHasFixedSize(true);
        CustomLayoutManager linearLayoutManager = new CustomLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new TweetAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                if (((CustomLayoutManager)recyclerView.getLayoutManager()).isOnNextPagePosition() && !isLoad) {
                    isLoad = true;
                    mainPresenter.loadMore(MAIN_TAG, adapter.getData().get(adapter.getData().size()-1).getId());
                }
            }
        });
        ((SimpleItemAnimator) recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    @Override
    public void showData(List<MyTweet> list) {
        progressBar.setVisibility(View.GONE);
        adapter.addData(list);
        isLoad = false;
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
