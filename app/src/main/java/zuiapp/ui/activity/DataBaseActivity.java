package zuiapp.ui.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.List;
import java.util.Random;

import androidz.util.ThreadUtil;
import timber.log.Timber;
import zuiapp.ZuiApp;
import zuiapp.database.DBConfig;
import zuiapp.database.room.dao.UserInfoDao;
import zuiapp.database.room.entity.UserInfo;
import zuiapp.database.sqlite.AppSQLiteDB;
import zuiapp.databinding.ActivityDatabaseBinding;

public class DataBaseActivity extends BaseActivity {
    private ActivityDatabaseBinding binding;
    private AppSQLiteDB database;
    private UserInfoDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDatabaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.toolbar.setTitle("DataBase");
        setSupportActionBar(binding.toolbar);

        database = new AppSQLiteDB(this, DBConfig.SQLITE_DB_NAME, DBConfig.SQLITE_DB_VERSION);
        userDao = ZuiApp.getDB().userDao();

        binding.buttonClear.setOnClickListener(v -> {
            Timber.d("clear ");
            // showMessage("");
            database.dropAllTables();
        });
        binding.buttonSqlInsert.setOnClickListener(v -> {
            Timber.d("sql insert ");
            SQLiteDatabase database1 = database.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("id", new Random().nextInt(100));
            database1.insert("User", null, values);
        });
        binding.buttonSqlQuery.setOnClickListener(v -> {
            Timber.d("sql query ");
            StringBuilder sb = new StringBuilder();
            try {
                SQLiteDatabase database2 = database.getReadableDatabase();
                Cursor cursor = database2.query("User", new String[]{"id", "username"},
                        " id < ? ", new String[]{"100"},
                        null, null, null);
                while (cursor.moveToNext()) {
                    UserInfo userInfo = new UserInfo();
                    userInfo.id = cursor.getInt(0);
                    userInfo.username = cursor.getString(1);
                    sb.append(userInfo).append("\n");
                }
                cursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            showMessage(sb.toString());
        });

        binding.buttonRoomInsert.setOnClickListener(v -> {
            Timber.d("room insert ");
            ThreadUtil.execute(() -> {
                UserInfo u = new UserInfo();
                u.id = new Random().nextInt(100);
                userDao.insert(u);
            });
        });
        binding.buttonRoomQuery.setOnClickListener(v -> {
            Timber.d("room query ");
            ThreadUtil.execute(() -> {
                List<UserInfo> users = userDao.queryAll();
                StringBuilder sb = new StringBuilder();
                for (UserInfo userInfo : users) {
                    sb.append(userInfo).append("\n");
                }
                runOnUiThread(() -> showMessage(sb.toString()));
            });
        });
    }

    private void showMessage(String textShow) {
        binding.textShow.setText(textShow);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}