package com.example.surveyapp.ui.survey

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.surveyapp.data.model.SurveyQuestion
import com.example.surveyapp.ui.survey.fragments.MultipleChoiceQuestionFragment
import com.example.surveyapp.ui.survey.fragments.TextInputQuestionFragment

class SurveyPagerAdapter(
    fa: FragmentActivity,
    private val questions: List<SurveyQuestion>
) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int = questions.size

    override fun createFragment(position: Int): Fragment {
        return when (questions[position].questionType) {
            SurveyQuestion.QuestionType.TEXT_INPUT -> {
                TextInputQuestionFragment.newInstance(questions[position])
            }
            SurveyQuestion.QuestionType.SINGLE_CHOICE,
            SurveyQuestion.QuestionType.MULTIPLE_CHOICE -> {
                MultipleChoiceQuestionFragment.newInstance(questions[position])
            }
            SurveyQuestion.QuestionType.RATING_SCALE -> {
                // Implementation for rating scale questions
                TextInputQuestionFragment.newInstance(questions[position])
            }
        }
    }

    fun getQuestionAt(position: Int): SurveyQuestion {
        return questions[position]
    }
}