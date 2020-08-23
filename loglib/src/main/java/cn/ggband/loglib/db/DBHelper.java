package cn.ggband.loglib.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.jetbrains.annotations.Nullable;


/**
 * 数据库辅助
 */
public class DBHelper extends SQLiteOpenHelper {
    // 数据库文件名
    public static final String DB_NAME = "dashboard.db";
    // 数据库版本号
    public static final int DB_VERSION = 1;

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createSql = "CREATE TABLE IF NOT EXISTS tb_cash_log (id integer PRIMARY KEY AUTOINCREMENT,version_code integer,version_name varchar,soft_version integer,app_name varchar,phone_model varchar,user_tag varchar,cash_name varchar,cash_detail varchar,cash_time TIMESTAMP DEFAULT (datetime('now', 'localtime')),is_reported INTEGER)";
        db.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
