package com.example.quizapp.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.MainActivity
import com.example.quizapp.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Quiz App"

        val sharedPreferences = getSharedPreferences("QuizAppPrefs", Context.MODE_PRIVATE)
        val score = sharedPreferences.getInt("SCORE", 0)
        val totalQuestions = sharedPreferences.getInt("TOTAL_QUESTIONS", 0)

        binding.txtYourScore.text = "Your score: $score / $totalQuestions"

        binding.btnBackToHome.setOnClickListener {

            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}