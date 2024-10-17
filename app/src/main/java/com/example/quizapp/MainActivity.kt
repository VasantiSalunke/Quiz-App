package com.example.quizapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.quizapp.activities.QuizActivity
import com.example.quizapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Quiz App"


        val sharedPreferences = getSharedPreferences("QuizAppPrefs", Context.MODE_PRIVATE)
        val highScore = sharedPreferences.getInt("HIGH_SCORE", 0)

        binding.txtHighScore.text = "High Score: $highScore"


        binding.btnStartQuiz.setOnClickListener {
            val intent = Intent(this, QuizActivity :: class.java)
            startActivity(intent)
        }



    }
}