package com.example.quizapp.service

import com.example.quizapp.model.Quiz
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET

interface QuizService {

    @GET("api.php?amount=10&category=18&type=multiple&encode=url3986")

     fun getQuiz() : Call<Quiz>

     companion object{

         fun getInstance() : QuizService {

             return Retrofit.Builder()
                 .baseUrl("https://opentdb.com/")
                 .addConverterFactory(GsonConverterFactory.create())
                 .build()
                 .create(QuizService::class.java)

         }
     }
}