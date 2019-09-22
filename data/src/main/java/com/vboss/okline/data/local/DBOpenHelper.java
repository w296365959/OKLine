package com.vboss.okline.data.local;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.ContactEntity;

import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/30 <br/>
 * Summary : 数据库操作辅助类
 */
class DBOpenHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "okline.db";
    private static final int DB_VERSION = 4;

    DBOpenHelper(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }

    DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CardEntity.CREATE_TABLE);
        db.execSQL(AppEntity.CREATE_TABLE);
        db.execSQL(ContactEntity.CREATE_TABLE);
        db.execSQL(CardLog.CREATE_TABLE);
    }

    /**
     * 删除本地数据库所有数据
     *
     * @param database
     */
    static void deleteDB(SQLiteDatabase database) throws SQLException {
        database.execSQL("DELETE FROM " + CardEntity.TABLE_NAME);
        database.execSQL("DELETE FROM " + AppEntity.TABLE_NAME);
        database.execSQL("DELETE FROM " + ContactEntity.TABLE_NAME);
        database.execSQL("DELETE FROM " + CardLog.TABLE_NAME);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Timber.tag("DBOpenHelper").i("onUpgrade# oldVersion = %d, newVersion = %d", oldVersion, newVersion);
        switch (newVersion) {
            case 3:
                //重建Card数据库
                db.execSQL(CardEntity.DROP_TABLE);
                db.execSQL(CardEntity.CREATE_TABLE);
            case 4:
                //重建App数据库
                db.execSQL(AppEntity.DROP_TABLE);
                db.execSQL(AppEntity.CREATE_TABLE);
                break;
            default:
                break;

        }
    }
}
