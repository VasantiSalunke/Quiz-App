package com.example.quizapp.activities

import QuizQuestionEntity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.quizapp.databinding.ActivityQuizBinding
import com.example.quizapp.model.Quiz
import com.example.quizapp.model.QuizQuestions
import com.example.quizapp.service.QuizService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

class QuizActivity : AppCompatActivity() {

    private lateinit var binding : ActivityQuizBinding
    private lateinit var questionsList : MutableList<QuizQuestions>
    private var currentQuestionIndex = 0
    private var score =0
    private var quizCompleted = false

    private var countDownTimer: CountDownTimer? = null
    private val totalTime = 30000L
    private val interval = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Quiz App"

        questionsList = mutableListOf()

        initListener()
        getQuestions()
        highestScore()




    }

    private fun initListener() {
        binding.btnNext.setOnClickListener {
            if (binding.rGroupAnswers.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Please select an answer", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            checkAnswer()

            if (currentQuestionIndex == questionsList.size - 1) {

                if (!quizCompleted) {
                    quizCompleted = true
                    launchSubmitActivity()
                }
            } else {
                currentQuestionIndex++
                displayQuestion()
            }
        }
    }

    private fun checkAnswer() {
        val selectedRbtn = binding.rGroupAnswers.checkedRadioButtonId
        if (selectedRbtn != -1) {
            val selectedAnswer = when (selectedRbtn) {
                binding.rbtnAnswer1.id -> binding.rbtnAnswer1.text.toString()
                binding.rbtnAnswer2.id -> binding.rbtnAnswer2.text.toString()
                binding.rbtnAnswer3.id -> binding.rbtnAnswer3.text.toString()
                binding.rbtnAnswer4.id -> binding.rbtnAnswer4.text.toString()
                else -> ""
            }

            val correctAnswer = questionsList[currentQuestionIndex].correct_answer

            if (selectedAnswer == correctAnswer) {
                score++
            }
        } else {
            Toast.makeText(this, "Please Select Answer", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getQuestions() {
        val quizService = QuizService.getInstance()
        quizService.getQuiz().enqueue(object : Callback<Quiz> {
            override fun onResponse(call: Call<Quiz>, response: Response<Quiz>) {
                if (response.isSuccessful) {
                    val quizResponse = response.body()
                    Log.d("QuizActivity", "API Response: $quizResponse")

                    val quizQuestions = quizResponse?.results ?: emptyList()


                    if (quizQuestions.isNotEmpty()) {

//                        val quizDatabase = QuizDatabase.getDatabase(this@QuizActivity)
//                        val quizQuestionDao = quizDatabase.quizQuestionDao()
//
//                        GlobalScope.launch(Dispatchers.IO) {
//                            quizQuestions.forEach { quizQuestion ->
//                                val quizQuestionEntity = QuizQuestionEntity(
//                                    category = quizQuestion.category,
//                                    correct_answer = quizQuestion.correct_answer,
//                                    difficulty = quizQuestion.difficulty,
//                                    question = quizQuestion.question,
//                                    type = quizQuestion.type,
//                                    incorrect_answers = quizQuestion.incorrect_answers
//                                )
//                                quizQuestionDao.insertQuizQuestion(quizQuestionEntity)
//                            }
//
//                        }

                        questionsList.addAll(quizQuestions)
                        displayQuestion()
                    } else {
                        Log.e("QuizActivity", "No quiz questions available")
                    }
                } else {
                    Log.e("QuizActivity", "Error in response: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<Quiz>, t: Throwable) {
                Log.e("QuizActivity", "Error fetching quiz: ${t.message}", t)
            }
        })
    }

            private fun displayQuestion() {
                if (questionsList.isNotEmpty() && currentQuestionIndex < questionsList.size) {

                    val currentQuestion = questionsList[currentQuestionIndex]


                    val decodedQuestion = decodeUrlEncodedString(currentQuestion.question)
                    binding.txtQuestion.text = decodedQuestion


                    val answerOptions = mutableListOf<String>().apply {
                        add(currentQuestion.correct_answer)
                        addAll(currentQuestion.incorrect_answers)
                    }.shuffled()

                    binding.rbtnAnswer1.text = decodeUrlEncodedString(answerOptions[0])
                    binding.rbtnAnswer2.text = decodeUrlEncodedString(answerOptions[1])
                    binding.rbtnAnswer3.text = decodeUrlEncodedString(answerOptions[2])
                    binding.rbtnAnswer4.text = decodeUrlEncodedString(answerOptions[3])

                    binding.rGroupAnswers.clearCheck()
                    startTimer()

                }
            }

    private fun launchSubmitActivity() {
        // Save score and other data in SharedPreferences
        val sharedPreferences = getSharedPreferences("QuizAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("SCORE", score)
        editor.putInt("TOTAL_QUESTIONS", questionsList.size)

        val previousHighScore = sharedPreferences.getInt("HIGH_SCORE", 0)
        if (score > previousHighScore) {
            editor.putInt("HIGH_SCORE", score)
        }
        editor.apply()

        val intent = Intent(this, SubmitActivity::class.java)
        startActivity(intent)
        finish()
    }



            private fun startTimer() {
                countDownTimer?.cancel()
                countDownTimer = object : CountDownTimer(totalTime, interval) {
                    override fun onTick(millisUntilFinished: Long) {
                        val secondsLeft = millisUntilFinished / 1000
                        binding.txtQuizTimer.text = "Time: $secondsLeft s"
                    }

                    override fun onFinish() {
                        binding.txtQuizTimer.text = "Time's up!"
                        currentQuestionIndex++
                        if (currentQuestionIndex < questionsList.size) {
                            displayQuestion()
                        } else {
                            if (!quizCompleted) {
                                quizCompleted = true
                                launchSubmitActivity()
                            }
                        }
                    }
                }.start()
            }

    private fun highestScore() {
        val sharedPreferences = getSharedPreferences("QuizAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val previousHighScore = sharedPreferences.getInt("HIGH_SCORE", 0)

        if (score > previousHighScore) {
            editor.putInt("HIGH_SCORE", score)
        }

        editor.apply()
    }


    private fun decodeUrlEncodedString(encodedString: String): String {
                return URLDecoder.decode(encodedString, StandardCharsets.UTF_8.toString())
            }
        }




