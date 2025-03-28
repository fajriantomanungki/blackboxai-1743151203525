package com.example.surveyapp.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.surveyapp.data.model.SurveyQuestion
import com.example.surveyapp.data.model.SurveyResponse
import com.example.surveyapp.data.dao.SurveyQuestionDao
import com.example.surveyapp.data.dao.SurveyResponseDao

@Database(entities = [SurveyQuestion::class, SurveyResponse::class], version = 1, exportSchema = false)
abstract class SurveyDatabase : RoomDatabase() {

    abstract fun surveyQuestionDao(): SurveyQuestionDao
    abstract fun surveyResponseDao(): SurveyResponseDao

    companion object {
        @Volatile
        private var INSTANCE: SurveyDatabase? = null

        fun getDatabase(context: Context): SurveyDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SurveyDatabase::class.java,
                    "survey_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}