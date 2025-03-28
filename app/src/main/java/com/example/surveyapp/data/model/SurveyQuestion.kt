package com.example.surveyapp.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class SurveyQuestion(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val questionText: String,
    val questionType: QuestionType,
    val options: List<String> = emptyList(),
    val isRequired: Boolean = true
) {
    enum class QuestionType {
        TEXT_INPUT,
        SINGLE_CHOICE,
        MULTIPLE_CHOICE,
        RATING_SCALE
    }
}