package com.example.surveyapp.data.sync

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.surveyapp.data.repository.SurveyRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SyncService @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: SurveyRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val unsyncedResponses = repository.getUnsyncedResponses()
                if (unsyncedResponses.isNotEmpty()) {
                    // Simulate network call to submit responses
                    submitResponses(unsyncedResponses)
                    repository.markResponsesAsSynced(unsyncedResponses.map { it.id })
                }
                Result.success()
            } catch (e: Exception) {
                Result.retry() // Retry on failure
            }
        }
    }

    private fun submitResponses(responses: List<SurveyResponse>) {
        // Implement the network call to submit responses to the server
        // This is a placeholder for actual network logic
    }
}