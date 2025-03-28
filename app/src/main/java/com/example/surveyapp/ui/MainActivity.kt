package com.example.surveyapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.surveyapp.databinding.ActivityMainBinding
import com.example.surveyapp.ui.survey.SurveyPagerAdapter
import com.example.surveyapp.ui.survey.SurveyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: SurveyViewModel by viewModels()
    private lateinit var pagerAdapter: SurveyPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupBottomNavigation()
        observeViewModel()
    }

    private fun setupViewPager() {
        pagerAdapter = SurveyPagerAdapter(this, emptyList())
        binding.viewPager.adapter = pagerAdapter
        binding.viewPager.isUserInputEnabled = false // Disable swipe navigation
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                com.example.surveyapp.R.id.nav_previous -> {
                    viewModel.moveToPreviousQuestion()
                    true
                }
                com.example.surveyapp.R.id.nav_next -> {
                    viewModel.moveToNextQuestion()
                    true
                }
                com.example.surveyapp.R.id.nav_submit -> {
                    // Handle submission
                    true
                }
                else -> false
            }
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.questions.collect { questions ->
                if (questions.isNotEmpty()) {
                    pagerAdapter = SurveyPagerAdapter(this@MainActivity, questions)
                    binding.viewPager.adapter = pagerAdapter
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.currentQuestionIndex.collect { position ->
                binding.viewPager.setCurrentItem(position, true)
                updateNavigationButtons(position)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.progress.collect { (answered, total) ->
                binding.progressIndicator.progress = (answered.toFloat() / total * 100).toInt()
            }
        }
    }

    private fun updateNavigationButtons(position: Int) {
        binding.bottomNavigation.menu.findItem(com.example.surveyapp.R.id.nav_previous).isEnabled = position > 0
        binding.bottomNavigation.menu.findItem(com.example.surveyapp.R.id.nav_next).isEnabled = 
            position < (pagerAdapter.itemCount - 1)
        binding.bottomNavigation.menu.findItem(com.example.surveyapp.R.id.nav_submit).isVisible = 
            position == pagerAdapter.itemCount - 1
    }
}