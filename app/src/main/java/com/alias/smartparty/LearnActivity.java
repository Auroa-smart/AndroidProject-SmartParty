package com.alias.smartparty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alias.smartparty.adapter.KnowledgeAdapter;
import com.alias.smartparty.db.KnowledgeDatabaseHelper;
import com.alias.smartparty.entity.Knowledge;
import com.alias.smartparty.service.MusicService;

import java.util.ArrayList;
import java.util.List;

public class LearnActivity extends AppCompatActivity {
    private Button indexButton;
    private RecyclerView recyclerView;
    private KnowledgeDatabaseHelper knowledgeHelper;
    private Intent musicIntent;
    private ImageView pauseButton;
    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);

        indexButton = findViewById(R.id.button_index);
        indexButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LearnActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // 知识点
        knowledgeHelper = new KnowledgeDatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Knowledge> knowledgeList = queryKnowledgeFromDatabase();
        Log.i("knowledgeList", "============================");
        Log.i("knowledgeList", String.valueOf(knowledgeList.size()));
        KnowledgeAdapter adapter = new KnowledgeAdapter(knowledgeList);
        recyclerView.setAdapter(adapter);

        // 背景音乐
        musicIntent = new Intent(this, MusicService.class);
        startService(musicIntent);
        pauseButton = findViewById(R.id.imageView_music);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    pauseButton.setImageResource(R.drawable.play);
                    pauseMusic();
                } else {
                    pauseButton.setImageResource(R.drawable.play);
                    playMusic();
                }
                isPlaying = !isPlaying;
            }
        });
    }

    private List<Knowledge> queryKnowledgeFromDatabase() {
        List<Knowledge> knowledgeList = new ArrayList<>();
        // 连接数据库
        SQLiteDatabase db = knowledgeHelper.getReadableDatabase();

        // 查询数据
        Cursor cursor = db.query("knowledge", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex("title"));
            @SuppressLint("Range") String content = cursor.getString(cursor.getColumnIndex("content"));
            @SuppressLint("Range") String picture = cursor.getString(cursor.getColumnIndex("picture"));
            Knowledge knowledge = new Knowledge(title, content, picture);
            knowledgeList.add(knowledge);
        }

        // 关闭数据库
        cursor.close();
        db.close();

        return knowledgeList;
    }

    private void pauseMusic() {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(MusicService.ACTION_PAUSE);
        startService(intent);
    }

    private void playMusic() {
        Intent intent = new Intent(this, MusicService.class);
        intent.setAction(MusicService.ACTION_PLAY);
        startService(intent);
    }


}
