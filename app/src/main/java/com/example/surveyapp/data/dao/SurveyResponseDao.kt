package com.example.surveyapp.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.surveyapp.data.model.SurveyResponse

@Dao
interface SurveyResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(response: SurveyResponse)

    @Update
    suspend fun update(response: SurveyResponse)

    @Query("SELECT * FROM responses WHERE questionId = :questionId")
    suspend fun getResponseForQuestion(questionId: Int): SurveyResponse?

    @Query("SELECT * FROM responses WHERE isSynced = 0")
    suspend fun getUnsyncedResponses(): List<SurveyResponse>

    @Query("UPDATE responses SET isSynced = 1 WHERE id IN (:ids)")
    suspend fun markResponsesAsSynced(ids: List<Int>)

    @Query("DELETE FROM responses WHERE questionId = :questionId")
    suspend fun deleteResponseForQuestion(questionId: Int)
}