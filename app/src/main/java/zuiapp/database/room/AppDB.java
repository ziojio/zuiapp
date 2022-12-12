package zuiapp.database.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.DeleteColumn;
import androidx.room.DeleteTable;
import androidx.room.RenameColumn;
import androidx.room.RenameTable;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.AutoMigrationSpec;
import zuiapp.database.DBConfig;
import zuiapp.database.room.dao.TrackLogDao;
import zuiapp.database.room.dao.UserInfoDao;
import zuiapp.database.room.entity.TrackLog;
import zuiapp.database.room.entity.UserInfo;

@Database(
        version = DBConfig.ROOM_DB_VERSION,
        entities = {
                UserInfo.class,
                TrackLog.class
        }
)
public abstract class AppDB extends RoomDatabase {

    @DeleteTable(tableName = "Album")
    @RenameTable(fromTableName = "Singer", toTableName = "Artist")
    @DeleteColumn(tableName = "Song", columnName = "genre")
    @RenameColumn(tableName = "Song", fromColumnName = "songName", toColumnName = "songTitle")
    static class MyAutoMigration implements AutoMigrationSpec {
    }


    public static AppDB create(Context appContext) {
        return Room.databaseBuilder(appContext, AppDB.class, DBConfig.ROOM_DB_NAME)
                .fallbackToDestructiveMigration()
                .build();
    }


    public abstract UserInfoDao userDao();

    public abstract TrackLogDao trackLogDao();

}
