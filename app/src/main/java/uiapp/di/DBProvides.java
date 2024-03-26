package uiapp.di;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import uiapp.UIApp;
import uiapp.database.room.AppDB;
import uiapp.database.room.entity.TrackLogDao;


@Module
@InstallIn(SingletonComponent.class)
public interface DBProvides {

    @Provides
    static AppDB DB() {
        return UIApp.getDB();
    }

    @Provides
    static TrackLogDao trackLogDao() {
        return UIApp.getDB().trackLogDao();
    }
}
