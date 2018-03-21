package app.my_group.com.myapplication.dagger.modules;

import javax.inject.Singleton;

import app.my_group.com.myapplication.common.managers.NetworkManager;
import dagger.Module;
import dagger.Provides;

@Module
public class ManagerModule {

    @Provides
    @Singleton
    NetworkManager provideNetworkManager() {
        return new NetworkManager();
    }
}