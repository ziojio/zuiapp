package uiapp.di;


import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import uiapp.UIApp;


@Module
@InstallIn(SingletonComponent.class)
public interface AppProvides {

    @Provides
    static UIApp App() {
        return UIApp.getApp();
    }


}
