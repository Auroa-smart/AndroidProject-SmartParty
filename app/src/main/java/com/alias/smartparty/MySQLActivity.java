package com.alias.smartparty;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MySQLActivity extends AppCompatActivity implements View.OnClickListener {
    private ListView listView;
    private MyHelper dbHelper;
    SQLiteDatabase db;
    SqlParser sqlParser;
    private Button btn_create, btn_check, btn_check2;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.look_main);

        btn_check = findViewById(R.id.btn_check);
        btn_check2 = findViewById(R.id.btn_check2);
        btn_create = findViewById(R.id.btn_create);
        textView = findViewById(R.id.text);

        //查询
        btn_create.setOnClickListener(this);
        btn_check.setOnClickListener(this);
        btn_check2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create:
                createDB();
                break;
            case R.id.btn_check:
                query1();
                break;
            case R.id.btn_check2:
                query2();
                break;
        }
    }

    public void createDB() {
        //创建数据库 和 相应表
        dbHelper = new MyHelper(this);
        //插入数据
        sqlParser = new SqlParser();
        sqlParser.parseSqlFile(this);
    }

    class SqlParser {
        public void parseSqlFile(Context context) {
            db = dbHelper.getWritableDatabase();
            String[] table = {"test.sql"};
            for (int i = 0; i < table.length; i++) {
                try {
                    InputStream is = context.getAssets().open(table[i]);
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
            dbHelper.close();

        }
    }

    public void query1() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query("knowledge", null, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String content = cursor.getString(cursor.getColumnIndex("content"));
                    Log.i("list", "id:" + id + "title:" + title);
                    textView.append(id + "" + title + "\n");
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        dbHelper.close();
    }

    public void query2() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.query("test", null, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String describe = cursor.getString(cursor.getColumnIndex("describe"));
                    String level = cursor.getString(cursor.getColumnIndex("level"));
                    int score = cursor.getInt(cursor.getColumnIndex("score"));
                    String optionA = cursor.getString(cursor.getColumnIndex("optionA"));
                    String optionB = cursor.getString(cursor.getColumnIndex("optionB"));
                    String optionC = cursor.getString(cursor.getColumnIndex("optionC"));
                    String optionD = cursor.getString(cursor.getColumnIndex("optionD"));
                    String answer = cursor.getString(cursor.getColumnIndex("answer"));
                    Log.i("list", "id:" + id + "level:" + level + score);
                    textView.append(id + "" + describe + "" + score + "\n");
                } while (cursor.moveToNext());
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        db.close();
        dbHelper.close();
    }
}
