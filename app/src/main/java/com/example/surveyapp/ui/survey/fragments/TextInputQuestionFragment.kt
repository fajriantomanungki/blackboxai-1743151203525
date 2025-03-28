package com.example.surveyapp.ui.survey.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.surveyapp.data.model.SurveyQuestion
import com.example.surveyapp.databinding.FragmentTextInputQuestionBinding
import com.example.surveyapp.ui.survey.SurveyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TextInputQuestionFragment : Fragment() {

    private var _binding: FragmentTextInputQuestionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SurveyViewModel by viewModels({ requireActivity() })

    private lateinit var question: SurveyQuestion

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTextInputQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        question = arguments?.getParcelable(ARG_QUESTION) ?: throw IllegalArgumentException("Question not found")
        setupUI()
    }

    private fun setupUI() {
        binding.questionText.text = question.questionText
        binding.submitButton.setOnClickListener {
            val answer = binding.answerInput.text.toString()
            if (answer.isNotBlank()) {
                val response = SurveyResponse(questionId = question.id, answer = answer)
                viewModel.saveResponse(response)
                // Navigate to next question
                viewModel.moveToNextQuestion()
            } else {
                // Show error message
                binding.answerInput.error = "This field is required"
            }
        }
    }

    companion object {
        private const val ARG_QUESTION = "question"

        fun newInstance(question: SurveyQuestion): TextInputQuestionFragment {
            val fragment = TextInputQuestionFragment()
            val args = Bundle().apply {
                putParcelable(ARG_QUESTION, question)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}