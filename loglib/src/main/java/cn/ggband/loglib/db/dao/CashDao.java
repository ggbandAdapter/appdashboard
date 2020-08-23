package cn.ggband.loglib.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.ggband.loglib.db.tb.TbCash;

/**
 * 异常表操作
 */
public class CashDao {

    private SQLiteDatabase db;

    public CashDao(SQLiteDatabase db) {
        this.db = db;
    }

    /**
     * 插入 cash
     */
    public long insertCash(TbCash cash) {
        ContentValues values = new ContentValues();
        values.put("version_code", cash.getVersionCode());
        values.put("version_name", cash.getVersionName());
        values.put("soft_version", cash.getSoftVersion());
        values.put("app_name", cash.getAppName());
        values.put("phone_model", cash.getPhoneModel());
        values.put("user_tag", cash.getUserTag());
        values.put("cash_name", cash.getCashName());
        values.put("cash_detail", cash.getCashDetail());
        values.put("is_reported", cash.getIsReported());
        return db.insert("tb_cash_log", "id", values);
    }

    /**
     * 更新 cash
     */
    public int updateCash(TbCash cash) {
        ContentValues values = new ContentValues();
        values.put("version_code", cash.getVersionCode());
        values.put("version_name", cash.getVersionName());
        values.put("soft_version", cash.getSoftVersion());
        values.put("app_name", cash.getAppName());
        values.put("phone_model", cash.getPhoneModel());
        values.put("user_tag", cash.getUserTag());
        values.put("cash_name", cash.getCashName());
        values.put("cash_detail", cash.getCashDetail());
        values.put("cash_time", cash.getCashTime());
        values.put("is_reported", cash.getIsReported());
        return db.update("tb_cash_log", values, "id=" + cash.getId(), null);
    }

    /**
     * 查询所有异常记录
     */
    public List<TbCash> getCashList() {
        List<TbCash> tbCashes = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM tb_cash_log ORDER BY cash_time DESC", null);
        while (cursor.moveToNext()) {
            TbCash user = new TbCash();
            user.setId(cursor.getInt(0));
            user.setVersionCode(cursor.getInt(1));
            user.setVersionName(cursor.getString(2));
            user.setSoftVersion(cursor.getInt(3));
            user.setAppName(cursor.getString(4));
            user.setPhoneModel(cursor.getString(5));
            user.setUserTag(cursor.getString(6));
            user.setCashName(cursor.getString(7));
            user.setCashDetail(cursor.getString(8));
            user.setCashTime(cursor.getString(9));
            user.setIsReported(cursor.getInt(10));
            tbCashes.add(user);
        }
        cursor.close();
        return tbCashes;
    }

    /**
     * 查询未上报的异常记录
     */
    public List<TbCash> getUnReportCashList() {
        List<TbCash> tbCashes = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM tb_cash_log WHERE is_reported=? ORDER BY cash_time DESC", new String[]{"0"});
        while (cursor.moveToNext()) {
            TbCash user = new TbCash();
            user.setId(cursor.getInt(0));
            user.setVersionCode(cursor.getInt(1));
            user.setVersionName(cursor.getString(2));
            user.setSoftVersion(cursor.getInt(3));
            user.setAppName(cursor.getString(4));
            user.setPhoneModel(cursor.getString(5));
            user.setUserTag(cursor.getString(6));
            user.setCashName(cursor.getString(7));
            user.setCashDetail(cursor.getString(8));
            user.setCashTime(cursor.getString(9));
            user.setIsReported(cursor.getInt(10));
            tbCashes.add(user);
        }
        cursor.close();
        return tbCashes;
    }

    /**
     * 删除
     */
    public int deleteCash(Integer id) {
        return db.delete("tb_cash_log", "id==" + id, null);
    }


}
