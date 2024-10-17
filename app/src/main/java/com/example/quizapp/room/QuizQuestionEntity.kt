import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_questions")
data class QuizQuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,
    val correct_answer: String,
    val difficulty: String,
    val question: String,
    val type: String,
    val incorrect_answers: String
)
