package uiapp.database.room.entity;

import uiapp.database.room.Converters;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity
public class TrackLog {

    @PrimaryKey(autoGenerate = true)
    public int id;
    public String label;
    public String tag;
    public String msg;
    @TypeConverters(Converters.class)
    public Date time;

    public TrackLog() {
    }

    @Ignore
    public TrackLog(String tag, String msg) {
        this.label = "App";
        this.tag = tag;
        this.msg = msg;
        this.time = new Date();
    }

    @Ignore
    public TrackLog(String label, String tag, String msg) {
        this.label = label;
        this.tag = tag;
        this.msg = msg;
        this.time = new Date();
    }

    @NonNull
    @Override
    public String toString() {
        return "TrackLog{" +
                "label='" + label + '\'' +
                ", tag='" + tag + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }

}
