import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QuizQuestionDao {

    @Insert
    suspend fun insertQuizQuestion(quizQuestionEntity: QuizQuestionEntity)

    @Query("SELECT * FROM quiz_questions")
    suspend fun getAllQuizQuestions(): List<QuizQuestionEntity>


}
