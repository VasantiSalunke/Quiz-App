package com.example.quizapp.model

data class Quiz(
    val response_code: Int,
    val results: List<QuizQuestions>
)