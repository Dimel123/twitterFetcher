package app.my_group.com.myapplication.main_screen.presenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.verification.VerificationMode;

import java.util.ArrayList;
import java.util.List;

import app.my_group.com.myapplication.database.MyTweet;
import app.my_group.com.myapplication.database.MyUser;
import app.my_group.com.myapplication.main_screen.view.MainActivity;
import app.my_group.com.myapplication.main_screen.view.MainView;
import io.reactivex.Flowable;

import static io.reactivex.Flowable.never;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock
    MainPresenter mainPresenter;

    @Mock
    MainView mainView;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mainPresenter.attachView(mainView);
    }

    private List<MyTweet> fakeList(){
        List<MyTweet> list = new ArrayList<>();
        list.add(new MyTweet(1, "test1", new MyUser()));
        list.add(new MyTweet(2, "test2", new MyUser()));
        list.add(new MyTweet(3, "test3", new MyUser()));
        return list;
    }

    @Test
    public void testUpload() throws Exception {
        List<MyTweet> fakeOrders = fakeList();
        when(mainPresenter.loadFromNet(MainActivity.MAIN_TAG, null)).thenReturn(Flowable.<List<MyTweet>>just(fakeOrders));
        mainPresenter.loadData(MainActivity.MAIN_TAG, null);
        verify(mainView).showProgress();
        verify(mainView).hideProgress();
        verify(mainView).showData(fakeOrders);
        verify(mainView, (VerificationMode) never()).showError(anyString());
    }

    @Test
    public void testUploadError() throws Exception {
        String error = "Network error";
        when(mainPresenter.loadFromNet(MainActivity.MAIN_TAG, null)).thenReturn(Flowable.<List<MyTweet>>error(new Exception(error)));
        mainPresenter.loadData(MainActivity.MAIN_TAG, null);
        verify(mainView).showProgress();
        verify(mainView).hideProgress();
        verify(mainView).showError(error);
        verify(mainView, (VerificationMode) never()).showData(ArgumentMatchers.<MyTweet>anyList());
    }
}