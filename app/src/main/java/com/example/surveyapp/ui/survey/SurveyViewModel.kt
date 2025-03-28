package com.example.surveyapp.ui.survey

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.surveyapp.data.model.SurveyQuestion
import com.example.surveyapp.data.model.SurveyResponse
import com.example.surveyapp.data.repository.SurveyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurveyViewModel @Inject constructor(
    private val repository: SurveyRepository
) : ViewModel() {

    private val _questions = MutableStateFlow<List<SurveyQuestion>>(emptyList())
    val questions: StateFlow<List<SurveyQuestion>> = _questions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _progress = MutableStateFlow<Pair<Int, Int>>(0 to 0)
    val progress: StateFlow<Pair<Int, Int>> = _progress.asStateFlow()

    private val _uiState = MutableStateFlow<SurveyUiState>(SurveyUiState.Loading)
    val uiState: StateFlow<SurveyUiState> = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        viewModelScope.launch {
            try {
                repository.getAllQuestions().collect { questions ->
                    _questions.value = questions
                    _uiState.value = SurveyUiState.Success
                    updateProgress()
                }
            } catch (e: Exception) {
                _uiState.value = SurveyUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun saveResponse(response: SurveyResponse) {
        viewModelScope.launch {
            repository.saveResponse(response)
            updateProgress()
        }
    }

    fun moveToNextQuestion() {
        _currentQuestionIndex.value = (_currentQuestionIndex.value + 1).coerceAtMost(_questions.value.size - 1)
    }

    fun moveToPreviousQuestion() {
        _currentQuestionIndex.value = (_currentQuestionIndex.value - 1).coerceAtLeast(0)
    }

    private fun updateProgress() {
        viewModelScope.launch {
            repository.getProgress().collect { progress ->
                _progress.value = progress
            }
        }
    }

    sealed class SurveyUiState {
        object Loading : SurveyUiState()
        object Success : SurveyUiState()
        data class Error(val message: String) : SurveyUiState()
    }
}