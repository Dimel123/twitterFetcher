package app.my_group.com.myapplication.dagger.component;

import javax.inject.Singleton;

import app.my_group.com.MyApplication;
import app.my_group.com.myapplication.dagger.modules.ApplicationModule;
import app.my_group.com.myapplication.dagger.modules.ManagerModule;
import app.my_group.com.myapplication.main_screen.view.MainActivity;
import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class, ManagerModule.class})
public interface AppComponent {

    MyApplication getApp();

    void inject(MainActivity activity);

}