package com.example.quizapp.model


data class QuizQuestions(
    val category: String,
    val correct_answer: String,
    val difficulty: String,
    val incorrect_answers: List<String>,
    val question: String,
    val type: String
){
    fun getAnswersOptions(): List<String> {
        val answers = mutableListOf<String>()
        answers.add(correct_answer)
        answers.addAll(incorrect_answers)
        answers.shuffle()
        return answers
    }
}