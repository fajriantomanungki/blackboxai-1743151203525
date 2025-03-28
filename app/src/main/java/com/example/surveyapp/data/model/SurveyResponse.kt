package com.example.surveyapp.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "responses",
    foreignKeys = [ForeignKey(
        entity = SurveyQuestion::class,
        parentColumns = ["id"],
        childColumns = ["questionId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["questionId"], unique = false)]
)
data class SurveyResponse(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val questionId: Int,
    val answer: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false
) {
    companion object {
        fun fromQuestion(question: SurveyQuestion, answer: String): SurveyResponse {
            return SurveyResponse(
                questionId = question.id,
                answer = answer
            )
        }
    }
}