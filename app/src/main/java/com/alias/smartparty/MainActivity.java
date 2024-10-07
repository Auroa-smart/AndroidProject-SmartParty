package com.alias.smartparty;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alias.smartparty.adapter.KnowledgeAdapter;
import com.alias.smartparty.db.KnowledgeDatabaseHelper;
import com.alias.smartparty.entity.Knowledge;
import com.alias.smartparty.service.MusicService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private MyHelper dbHelper;

    private EditText edt_search;
    private Button btn_search, btn_goStudy, btn_goTest, btn_mine;
    private RecyclerView recyclerView;
    private ImageView img_music, img_left, img_right, img_center;
    private int pic_last, pic_next, pic_current;
    private TextView textView_title, totalScoreView;
    private int[] pics = {R.drawable.pic1, R.drawable.pic2, R.drawable.pic3, R.drawable.pic4};
    private AnimationDrawable anim;
    private static int totalScore = 1;//记录当前用户的总积分

    List<Integer> imageIds = new ArrayList<>();
    private ViewPager viewPager;
    private ImagePagerAdapter adapter;
    private Timer mTimer;
    private int mCurrentPage = 0;

    private boolean isPlaying = true;//记录音乐播放状态

    private KnowledgeDatabaseHelper knowledgeHelper;
    private Intent musicIntent;
    final static String TAG = "score";
    static boolean hasCreated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handleDatabase();

        init();
    }

    private void handleDatabase() {
        String DB_PATH = "/data/data/com.alias.smartparty/databases/";
        String DB_NAME = "smart";

        // 检查 SQLite 数据库文件是否存在
        if ((new File(DB_PATH + DB_NAME)).exists() == false) {
            // 如 SQLite 数据库文件不存在，再检查一下 database 目录是否存在
            File f = new File(DB_PATH);
            // 如 database 目录不存在，新建该目录
            if (!f.exists()) {
                f.mkdir();
            }

            try {
                // 得到 assets 目录下我们实现准备好的 SQLite 数据库作为输入流
                InputStream is = getBaseContext().getAssets().open(DB_NAME);
                // 输出流
                OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);

                // 文件写入
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }

                // 关闭文件流
                os.flush();
                os.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 下面测试 /data/data/com.test.db/databases/ 下的数据库是否能正常工作
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DB_PATH + DB_NAME, null);
        Cursor cursor = database.rawQuery("select * from test", null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            // 看输出的信息是否正确
            System.out.println(cursor.getColumnCount());
        }
        cursor.close();
    }

    public void init() {
        edt_search = findViewById(R.id.editView_search);
        totalScoreView = findViewById(R.id.textView_totalScore);
        totalScoreView.setText(totalScore + "");
//        btn_search = findViewById(R.id.button_search);
        img_music = findViewById(R.id.imageView_music);
//        img_left = findViewById(R.id.imageView_left);
//        img_right = findViewById(R.id.imageView_right);
//        img_center = findViewById(R.id.imageView_center);
        btn_goStudy = findViewById(R.id.button_goStudy);
        btn_goTest = findViewById(R.id.button_goTest);
        recyclerView = findViewById(R.id.recyclerView);
        viewPager = findViewById(R.id.viewPager);
        textView_title = findViewById(R.id.textView_title);
        dbHelper = new MyHelper(this);


        //图片的id
        imageIds.add(R.drawable.pic1);
        imageIds.add(R.drawable.pic2);
        imageIds.add(R.drawable.pic3);
        imageIds.add(R.drawable.pic4);
        adapter = new ImagePagerAdapter(this, imageIds);
        viewPager.setAdapter(adapter);

//        img_left.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                int pic_current = viewPager.getCurrentItem();
//                if (pic_current == 0 ){
//                    pic_last = 3;
//                }else{
//                    pic_last = pic_current - 1;
//                }
//                viewPager.setCurrentItem(pic_last);
//            }
//        });
//        img_right.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                int pic_current = viewPager.getCurrentItem();
//                pic_next = (pic_current+1)%3;
//                viewPager.setCurrentItem(pic_next);
//            }
//        });

        //为viewPager添加监听器
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        btn_goStudy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LearnActivity.class);
                startActivity(intent);
            }
        });
        btn_goTest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TestActivity.class);
                //启动跳转，这里使用startActivityForResult（）方法，这个方法会在跳转到的活动页面销毁时，返回一个数据给上一个活动
                /**
                 * param1:intent
                 * param2：请求码，用于回调时判断数据的来源
                 */
                startActivityForResult(intent, 1);
//                startActivity(intent);
            }
        });


        // 知识点
        knowledgeHelper = new KnowledgeDatabaseHelper(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Knowledge> knowledgeList = queryKnowledgeFromDatabase();
        Log.i("knowledgeList", "============================");
        Log.i("knowledgeList", String.valueOf(knowledgeList.size()));
        KnowledgeAdapter adapter = new KnowledgeAdapter(knowledgeList);
        recyclerView.setAdapter(adapter);

        // 背景音乐
        musicIntent = new Intent(this, MusicService.class);
        startService(musicIntent);


        img_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    img_music.setImageResource(R.drawable.play);
                    pauseMusic();
                } else {
                    img_music.setImageResource(R.drawable.play);
                    playMusic();
                }
                isPlaying = !isPlaying;
            }
        });

        //启动图片轮播
//        anim = (AnimationDrawable) img_center.getBackground();
//        anim.start();


/*
        //左滑动
        img_left.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //获取当前帧
                currentFrame = anim.getCurrent();
                for (int i = 0; i < anim.getNumberOfFrames(); i++) {
                    checkFrame = anim.getFrame(i);
                    if (checkFrame == currentFrame) {
                        pic_current = i;
                        break;
                    }
                }
                Log.i("anim","pic_current"+pic_current);
                if (pic_current == 0 ){
                    pic_last = 3;
                }else{
                    pic_last = pic_current - 1;
                }
                Log.i("anim","pic_last"+pic_last);
//                anim.stop();
//                img_center.setImageResource();
                anim.selectDrawable(pic_last);

            }
        });

        //右滑动
        img_right.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                currentFrame = anim.getCurrent();
                for (int i = 0; i < anim.getNumberOfFrames(); i++) {
                    checkFrame = anim.getFrame(i);
                    if (checkFrame == currentFrame) {
                        pic_current = i;
                        break;
                    }
                }
                Log.i("anim","pic_current"+pic_current);

                pic_next = (pic_current+1)%3;
                Log.i("anim","pic_next"+pic_next);
                anim.selectDrawable(pic_next);
            }
        });
        */
    }

    private List<Knowledge> queryKnowledgeFromDatabase() {
        List<Knowledge> knowledgeList = new ArrayList<>();
        // 连接数据库
        SQLiteDatabase db = knowledgeHelper.getReadableDatabase();

        // 查询数据
        Cursor cursor = db.query("knowledge", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
//            int id = cursor.getInt(cursor.getColumnIndex("id"));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e(TAG, "onActivityResult1: " + data);
        switch (resultCode) {
            //下面的1为startActivityForResult(intent, 1);中的1
            case 1:
                //这里的1为setResult(1, intent);中的1
                if (resultCode == 1) {
                    int score = data.getIntExtra("addScore", 0);
                    Log.i("score", "score" + score);
                    totalScore += score;
                    totalScoreView.setText(totalScore + "");
                    Log.i("score", "totalScore" + totalScore);

                }
                break;
            default:
                break;

        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("score", "onResume");
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTimer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(musicIntent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Log.i("score", "onRestart");
        startService(musicIntent);
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

    private void startTimer() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mCurrentPage == imageIds.size() - 1) {
                            mCurrentPage = 0;
                        } else {
                            mCurrentPage++;
                        }
                        viewPager.setCurrentItem(mCurrentPage);
                    }
                });
            }
        }, 5000, 5000);
    }

    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


}
