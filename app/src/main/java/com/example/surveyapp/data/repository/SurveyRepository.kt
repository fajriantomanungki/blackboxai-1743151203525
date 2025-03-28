package com.example.surveyapp.data.repository

import com.example.surveyapp.data.database.SurveyDatabase
import com.example.surveyapp.data.model.SurveyQuestion
import com.example.surveyapp.data.model.SurveyResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SurveyRepository(private val database: SurveyDatabase) {

    // Question operations
    suspend fun insertQuestion(question: SurveyQuestion) {
        database.surveyQuestionDao().insert(question)
    }

    suspend fun getQuestionById(id: Int): SurveyQuestion? {
        return database.surveyQuestionDao().getQuestionById(id)
    }

    fun getAllQuestions(): Flow<List<SurveyQuestion>> {
        return database.surveyQuestionDao().getAllQuestions()
    }

    // Response operations
    suspend fun saveResponse(response: SurveyResponse) {
        database.surveyResponseDao().insert(response)
    }

    suspend fun getResponseForQuestion(questionId: Int): SurveyResponse? {
        return database.surveyResponseDao().getResponseForQuestion(questionId)
    }

    suspend fun getUnsyncedResponses(): List<SurveyResponse> {
        return database.surveyResponseDao().getUnsyncedResponses()
    }

    suspend fun markResponsesAsSynced(ids: List<Int>) {
        database.surveyResponseDao().markResponsesAsSynced(ids)
    }

    // Combined operations
    suspend fun getQuestionWithResponse(questionId: Int): Pair<SurveyQuestion, SurveyResponse?> {
        val question = getQuestionById(questionId) ?: throw IllegalArgumentException("Question not found")
        val response = getResponseForQuestion(questionId)
        return Pair(question, response)
    }

    suspend fun getProgress(): Flow<Pair<Int, Int>> {
        return database.surveyQuestionDao().getAllQuestions().map { questions ->
            val total = questions.size
            val answered = database.surveyResponseDao().getResponseCount()
            Pair(answered, total)
        }
    }
}