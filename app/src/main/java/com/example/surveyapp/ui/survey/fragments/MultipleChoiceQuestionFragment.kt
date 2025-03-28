package com.example.surveyapp.ui.survey.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.surveyapp.data.model.SurveyQuestion
import com.example.surveyapp.databinding.FragmentMultipleChoiceQuestionBinding
import com.example.surveyapp.ui.survey.SurveyViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MultipleChoiceQuestionFragment : Fragment() {

    private var _binding: FragmentMultipleChoiceQuestionBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SurveyViewModel by viewModels({ requireActivity() })

    private lateinit var question: SurveyQuestion

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMultipleChoiceQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        question = arguments?.getParcelable(ARG_QUESTION) ?: throw IllegalArgumentException("Question not found")
        setupUI()
    }

    private fun setupUI() {
        binding.questionText.text = question.questionText
        
        // Clear any existing options
        binding.optionsContainer.removeAllViews()
        
        // Add options based on question type
        question.options.forEach { option ->
            when (question.questionType) {
                SurveyQuestion.QuestionType.SINGLE_CHOICE -> {
                    // Add radio button for single choice
                    val radioButton = com.google.android.material.radio.MaterialRadioButton(requireContext()).apply {
                        text = option
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    }
                    binding.optionsContainer.addView(radioButton)
                }
                SurveyQuestion.QuestionType.MULTIPLE_CHOICE -> {
                    // Add checkbox for multiple choice
                    val checkBox = com.google.android.material.checkbox.MaterialCheckBox(requireContext()).apply {
                        text = option
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                        )
                    }
                    binding.optionsContainer.addView(checkBox)
                }
                else -> {}
            }
        }

        binding.submitButton.setOnClickListener {
            val selectedOptions = getSelectedOptions()
            if (selectedOptions.isNotEmpty() || !question.isRequired) {
                val response = SurveyResponse(
                    questionId = question.id,
                    answer = selectedOptions.joinToString("|")
                )
                viewModel.saveResponse(response)
                viewModel.moveToNextQuestion()
            } else {
                // Show error message
                binding.errorText.visibility = View.VISIBLE
                binding.errorText.text = "Please select at least one option"
            }
        }
    }

    private fun getSelectedOptions(): List<String> {
        val selectedOptions = mutableListOf<String>()
        for (i in 0 until binding.optionsContainer.childCount) {
            val child = binding.optionsContainer.getChildAt(i)
            when (child) {
                is com.google.android.material.radio.MaterialRadioButton -> {
                    if (child.isChecked) {
                        selectedOptions.add(child.text.toString())
                    }
                }
                is com.google.android.material.checkbox.MaterialCheckBox -> {
                    if (child.isChecked) {
                        selectedOptions.add(child.text.toString())
                    }
                }
            }
        }
        return selectedOptions
    }

    companion object {
        private const val ARG_QUESTION = "question"

        fun newInstance(question: SurveyQuestion): MultipleChoiceQuestionFragment {
            val fragment = MultipleChoiceQuestionFragment()
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