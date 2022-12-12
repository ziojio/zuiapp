package zuiapp.database.room.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import zuiapp.database.room.entity.TrackLog;


@Dao
public abstract class TrackLogDao {

    @Query("select * from TrackLog")
    public abstract List<TrackLog> queryAll();

    @Query("select * from TrackLog where id=:id")
    public abstract TrackLog queryById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<TrackLog> users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(TrackLog trackLog);

    @Update
    public abstract void update(TrackLog trackLog);

    @Delete
    public abstract void delete(TrackLog trackLog);

}
