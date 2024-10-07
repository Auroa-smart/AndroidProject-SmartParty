package com.alias.smartparty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.alias.smartparty.entity.Question;
import com.alias.smartparty.service.MusicService;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestActivity extends AppCompatActivity {
    private TextView questionTextView;
    private RadioGroup answerRadioGroup;
    private Button submitButton;
    private Button returnButton;
    private TextView processTextView;
    private TextView levelTextView;
    private TextView scoreTextView;

    private int score = 0;
    private int currentQuestionIndex = 0;
    private List<Question> questionList = new ArrayList<>();
    private List<Integer> questionIndexList = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // 初始化UI元素
        questionTextView = findViewById(R.id.questionTextView);
        answerRadioGroup = findViewById(R.id.answerRadioGroup);
        submitButton = findViewById(R.id.submitButton);
        returnButton = findViewById(R.id.returnButton);
        processTextView = findViewById(R.id.processTextView);
        levelTextView = findViewById(R.id.levelTextView);
        scoreTextView = findViewById(R.id.scoreTextView);

        // 创建数据库
        MyHelper questionDatabaseHelper = new MyHelper(this);
        // 从数据库中加载问题列表
        questionList = questionDatabaseHelper.queryAll(questionDatabaseHelper.getReadableDatabase());

        // 随机选择3个问题
        Random random = new Random();
        while (questionIndexList.size() < 3) {
            int index = random.nextInt(questionList.size());
            if (!questionIndexList.contains(index)) {
                questionIndexList.add(index);
            }
        }

        // 显示第一个问题
        showQuestion(currentQuestionIndex);
        submitButton.setText("下一题");


        // 设置提交按钮的单击事件
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 检查用户的答案，更新得分
                checkAnswer();

                // 显示下一个问题或显示得分
                currentQuestionIndex++;
                if (currentQuestionIndex < questionIndexList.size()) {
                    showQuestion(currentQuestionIndex);
                    if (currentQuestionIndex == questionIndexList.size()-1)
                        submitButton.setText("提交");
                } else {
                    showScore();
                }
            }
        });

        // 设置返回按钮的单击事件
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 返回主界面
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("addScore", score);
//                startActivity(intent);
                setResult(1, intent);
                finish();
            }
        });
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

    // 显示问题
    private void showQuestion(int index) {
        Question question = questionList.get(questionIndexList.get(index));
        questionTextView.setText(question.getDescribe());
        answerRadioGroup.removeAllViews();
        for (String option : question.getOptions()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            answerRadioGroup.addView(radioButton);
        }
        String p = index+1 + "/3";
        processTextView.setText(p);
        String levelInfo = "难度："+question.getLevel();
        String scoreInfo = "积分：" +question.getScore();
        levelTextView.setText(levelInfo);
        levelTextView.setTextSize(18);
        scoreTextView.setText(scoreInfo);
        scoreTextView.setTextSize(18);
    }

    // 检查用户的答案是否正确
    private void checkAnswer() {
        Question question = questionList.get(questionIndexList.get(currentQuestionIndex));
        int checkedRadioButtonId = answerRadioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId != -1) {
            RadioButton checkedRadioButton = findViewById(checkedRadioButtonId);
            String checkedAnswer = checkedRadioButton.getText().toString();
            if (checkedAnswer.equals(question.getAnswer())) {
                score += question.getScore();
            }
        }
    }

    // 显示得分
    private void showScore() {
        questionTextView.setText("您的得分是：" + score);
        answerRadioGroup.setVisibility(View.GONE);
        submitButton.setVisibility(View.GONE);
        returnButton.setVisibility(View.VISIBLE);
        processTextView.setVisibility(View.GONE);
        levelTextView.setVisibility(View.GONE);
        scoreTextView.setVisibility(View.GONE);
    }
}