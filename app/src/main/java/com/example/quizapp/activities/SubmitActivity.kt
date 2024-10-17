package com.example.quizapp.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.databinding.ActivitySubmitBinding

class SubmitActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubmitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySubmitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Quiz App"

        binding.btnViewResult.setOnClickListener {
            val intent = Intent(this,ResultActivity ::class.java)
            startActivity(intent)
        }
    }


}