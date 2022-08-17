package com.ziojio.zuiapp.database.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class AppSQLiteDB extends SQLiteDB {

    public AppSQLiteDB(@Nullable Context context, @Nullable String name, int version) {
        super(context, name, version);
    }

    @Override
    protected void createTables(SQLiteDatabase db) {
        String tbl_user = "CREATE TABLE IF NOT EXISTS `User`(" +
                " `id`       INTEGER PRIMARY KEY NOT NULL," +
                " `username` TEXT," +
                " `password` TEXT," +
                " `email`    TEXT," +
                " `phone`    TEXT)";
        db.execSQL(tbl_user);
    }

}
