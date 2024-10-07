package com.alias.smartparty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alias.smartparty.entity.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

class MyHelper extends SQLiteOpenHelper {
    Context context2;
    //必须加上构造方法
    MyHelper(Context context){
        //定义数据库的文件名和版本号
        super(context, "smart", null, 1);//user为文件名 数据库名; 游标工厂  版本号
        Log.i("list","Myhelper ");
        this.context2 = context;
    }
    class SqlParser {
        public void parseSqlFile(Context context) {


        }
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table knowledge(id integer PRIMARY KEY AUTOINCREMENT , title varchar(50), content varchar(250),picture varchar(20))");
        db.execSQL("create table test(id integer PRIMARY KEY AUTOINCREMENT , describe varchar(50), level varchar(5),score int , optionA varchar(30), optionB varchar(30), optionC varchar(30), optionD varchar(30),answer varchar(30))");

//        db = dbHelper.getWritableDatabase();
        String[] table = {"knowledge.sql", "test.sql"};
        for (int i = 0; i < table.length; i++) {
            try {
                InputStream is = context2.getAssets().open(table[i]);
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().length() > 0) {
                        db.execSQL(line);
                    }
                }
                Log.i("list", "成功");
                reader.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("list", "失败");
            }
        }
        db.close();
//        dbHelper.close();
        Log.i("list","Myhelper create");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public ArrayList<Question> queryAll(SQLiteDatabase db) {
        ArrayList<Question> questionList = new ArrayList<>();
        Cursor cursor = db.query("test", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") Integer id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String describe = cursor.getString(cursor.getColumnIndex("describe"));
            @SuppressLint("Range") String level = cursor.getString(cursor.getColumnIndex("level"));
            @SuppressLint("Range") Integer score = cursor.getInt(cursor.getColumnIndex("score"));
            @SuppressLint("Range") String optionA = cursor.getString(cursor.getColumnIndex("optionA"));
            @SuppressLint("Range") String optionB = cursor.getString(cursor.getColumnIndex("optionB"));
            @SuppressLint("Range") String optionC = cursor.getString(cursor.getColumnIndex("optionC"));
            @SuppressLint("Range") String optionD = cursor.getString(cursor.getColumnIndex("optionD"));
            @SuppressLint("Range") String answer = cursor.getString(cursor.getColumnIndex("answer"));
            Question question = new Question(id,describe, level, score, optionA, optionB, optionC, optionD, answer);
            questionList.add(question);
        }
        return questionList;
    }
}

