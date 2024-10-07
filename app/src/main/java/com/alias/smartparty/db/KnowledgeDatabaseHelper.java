package com.alias.smartparty.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KnowledgeDatabaseHelper extends SQLiteOpenHelper {

    private static final String CREATE_KNOWLEDGE = "create table knowledge ("
            + "id integer primary key autoincrement, "
            + "title text, "
            + "content text, "
            + "picture text)";

    public KnowledgeDatabaseHelper(Context context) {
        super(context, "smart", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_KNOWLEDGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 升级数据库
    }
}
