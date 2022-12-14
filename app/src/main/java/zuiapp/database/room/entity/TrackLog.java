package zuiapp.database.room.entity;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;
import zuiapp.database.room.Converters;

@Entity
public class TrackLog {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String type;
    public String name;
    public String data;
    @TypeConverters(Converters.class)
    public Date time;
    public String tag;

    public TrackLog() {
    }

    @Ignore
    public TrackLog(String type, String name, String data) {
        this.type = type;
        this.name = name;
        this.data = data;
        this.time = new Date();
    }

    @Override
    public String toString() {
        return "TrackLog{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", data='" + data + '\'' +
                ", time=" + time +
                ", tag='" + tag + '\'' +
                '}';
    }
}
