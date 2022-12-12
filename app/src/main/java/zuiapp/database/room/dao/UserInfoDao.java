package zuiapp.database.room.dao;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import zuiapp.database.room.entity.UserInfo;


@Dao
public abstract class UserInfoDao {

    @Query("select * from UserInfo")
    public abstract List<UserInfo> queryAll();

    @Query("select * from UserInfo where id=:id")
    public abstract UserInfo queryById(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(List<UserInfo> users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(UserInfo user);

    @Update
    public abstract void update(UserInfo user);

    @Delete
    public abstract void delete(UserInfo user);

}
