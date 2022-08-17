package com.ziojio.zuiapp.database.sqlite;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class SQLiteDB extends SQLiteOpenHelper {
    private boolean debug;

    public SQLiteDB(@Nullable Context context, @Nullable String name,
                    @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        initDb(context);
    }

    public SQLiteDB(@Nullable Context context, @Nullable String name,
                    @Nullable SQLiteDatabase.CursorFactory factory, int version, @Nullable DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
        initDb(context);
    }

    public SQLiteDB(@Nullable Context context, @Nullable String name, int version) {
        super(context, name, null, version, null);
        initDb(context);
    }

    private void initDb(@Nullable Context context) {
        debug = context != null && (context.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        d("onCreate ");
        createTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        d("onUpgrade oldVersion=" + oldVersion + ", newVersion=" + newVersion);
        dropAllTables(db);
        createTables(db);
    }

    protected void createTables(SQLiteDatabase db) {

    }

    /**
     * @param tableSql CREATE TABLE IF NOT EXISTS `TableName`(
     *                 `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
     *                 `columnName` TEXT)
     */
    public void createTable(SQLiteDatabase db, @NonNull String tableSql) {
        d("createTable " + tableSql);
        try {
            db.execSQL(tableSql);
        } catch (Exception e) {
            d(e);
        }
    }

    public List<String> queryAllTableNames() {
        return queryAllTableNames(getWritableDatabase());
    }

    @NonNull
    public List<String> queryAllTableNames(SQLiteDatabase db) {
        List<String> tables = new ArrayList<>();
        try (Cursor cursor = db.query("sqlite_master", new String[]{"name"},
                "type=?", new String[]{"table"},
                null, null, null)) {
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(0);
                    if (name.equals("android_metadata"))
                        continue;
                    tables.add(name);
                } while (cursor.moveToNext());
            }
        }
        d("queryAllTableNames " + tables);
        return tables;
    }

    public void dropAllTables() {
        dropAllTables(getWritableDatabase());
    }

    public void dropAllTables(SQLiteDatabase db) {
        List<String> tables = queryAllTableNames(db);
        d("dropAllTables " + tables);
        for (String name : tables) {
            dropTable(db, name);
        }
    }

    public void dropTable(String tableName) {
        dropTable(getWritableDatabase(), tableName);
    }

    public void dropTable(SQLiteDatabase db, String tableName) {
        d("dropTable " + tableName);
        db.execSQL("DROP TABLE IF EXISTS `" + tableName + "`");
    }

    public void clearAllTables() {
        clearAllTables(getWritableDatabase());
    }

    public void clearAllTables(SQLiteDatabase db) {
        List<String> tables = queryAllTableNames(db);
        d("clearAllTables " + tables);
        for (String name : tables) {
            clearTable(db, name);
        }
    }

    public void clearTable(String tableName) {
        clearTable(getWritableDatabase(), tableName);
    }

    public void clearTable(SQLiteDatabase db, String tableName) {
        d("clearTable " + tableName);
        try {
            db.beginTransaction();
            db.execSQL("DELETE FROM `" + tableName + "`");
            db.setTransactionSuccessful();
        } catch (Exception e) {
            d(e);
        } finally {
            db.endTransaction();
        }
    }

    private void d(String msg) {
        if (debug) Timber.d(msg);
    }

    private void d(Throwable t) {
        if (debug) Timber.d(t);
    }

}
