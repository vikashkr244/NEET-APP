package com.example.myquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myquiz.databinding.ActivityQuizBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuizActivity extends AppCompatActivity {
ActivityQuizBinding binding;
ArrayList<Questions> questions;
int index =0;
    Questions question;
    CountDownTimer timer;
    FirebaseFirestore database;
    int correctAnswers=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        questions = new ArrayList<>();
        database = FirebaseFirestore.getInstance();
        String catId = getIntent().getStringExtra("catId");

        database.collection("categories")
                .document(catId)
                .collection("questions")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                        int totalQuestions = documents.size();

                        if (totalQuestions <= 10) {
                            // Load all available questions if there are less than or equal to 10
                            for (DocumentSnapshot snapshot : documents) {
                                Questions question = snapshot.toObject(Questions.class);
                                questions.add(question);
                            }
                        } else {
                            // Randomly select 10 questions from the collection
                            Random random = new Random();
                            Set<Integer> selectedIndexes = new HashSet<>();

                            while (selectedIndexes.size() < 10) {
                                int randomIndex = random.nextInt(totalQuestions);
                                if (!selectedIndexes.contains(randomIndex)) {
                                    selectedIndexes.add(randomIndex);
                                    Questions question = documents.get(randomIndex).toObject(Questions.class);
                                    questions.add(question);
                                }
                            }
                        }

                        setNextQuestion();
                    }

                });

        restTimer();
    }
    private boolean optionSelected = false;

    void restTimer(){
        timer = new CountDownTimer(30000,1000) {
            @Override
            public void onTick(long l) {
                binding.timer.setText(String.valueOf(l/1000));
            }

            @Override
            public void onFinish() {
                setNextQuestion();
            }
        };
    }

    void showAnswer(){
        if(question.getAns().equals(binding.option1.getText().toString()))
            binding.option1.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAns().equals(binding.option2.getText().toString()))
            binding.option2.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAns().equals(binding.option3.getText().toString()))
            binding.option3.setBackground(getResources().getDrawable(R.drawable.option_right));
        else if(question.getAns().equals(binding.option4.getText().toString()))
            binding.option4.setBackground(getResources().getDrawable(R.drawable.option_right));

    }
    void setNextQuestion(){
        optionSelected=false;
        if(timer!= null)
            timer.cancel();

        restTimer();
        timer.start();
        if(index<questions.size()){
            binding.questionCounter.setText(String.format("%d/%d",(index+1),questions.size()));
            question =questions.get(index);
            binding.question.setText(question.getQuestion());
            binding.option1.setText(question.getOpt1());
            binding.option2.setText(question.getOpt2());
            binding.option3.setText(question.getOpt3());
            binding.option4.setText(question.getOpt4());
              index++;
        }
    }

    void  checkAnswer(TextView textView){
        if (!optionSelected) {
            optionSelected = true; // Set the flag to true to indicate an option has been selected
            String selectedAnswer = textView.getText().toString();
            if (selectedAnswer.equals(question.getAns())) {
                correctAnswers++;
                Toast.makeText(this, "CORRECT!!", Toast.LENGTH_SHORT).show();
                textView.setBackground(getResources().getDrawable(R.drawable.option_right));


            } else {
                showAnswer();
                Toast.makeText(this, "WRONG!!!", Toast.LENGTH_SHORT).show();
                textView.setBackground(getResources().getDrawable(R.drawable.option_wrong));
            }
        }
    }
    void reset(){
        binding.option1.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option2.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option3.setBackground(getResources().getDrawable(R.drawable.option_unselected));
        binding.option4.setBackground(getResources().getDrawable(R.drawable.option_unselected));
    }
    public void onClick(View view){
        switch (view.getId()){
            case R.id.option_1:
            case R.id.option_2:
            case R.id.option_3:
            case R.id.option_4:
                if(timer!= null)
                    timer.cancel();
                TextView selected = (TextView) view;
                checkAnswer(selected);
               break;
            case  R.id.nextBtn:
             // reset();
                if(index<questions.size()) {
                    reset();
                    setNextQuestion();

                }else {
                    Intent intent = new Intent(QuizActivity.this,ResultActivity.class);
                    intent.putExtra("correct",correctAnswers);
                    intent.putExtra("total",questions.size());
                    startActivity(intent);
                    Toast.makeText(this, "Quiz Finished", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}