package com.example.quizapp.fragments

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentResultBinding
import com.example.quizapp.models.Quiz
import com.google.gson.Gson

class ResultFragment : Fragment() {

    private lateinit var quiz: Quiz
    private lateinit var binding: FragmentResultBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
    }

    private fun setUpViews() {
        val quizData = arguments?.getString("QUIZ")
        quiz = Gson().fromJson(quizData, Quiz::class.java)
        calculateScore()
        setAnswerView()
    }

    private fun setAnswerView() {
        val builder = StringBuilder("")
        for (entry in quiz.questions.entries) {
            val question = entry.value
            builder.append("<font color'#18206F'><b>Question: ${question.description}</b></font><br/><br/>")
            builder.append("<font color='#009688'>Answer: ${question.answer}</font><br/><br/>")
        }
        binding.txtAnswer.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(builder.toString(), Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(builder.toString())
        }
    }

    private fun calculateScore() {
        var score = 0
        for (entry in quiz.questions.entries) {
            val question = entry.value
            if (question.answer == question.userAnswer) {
                score += 10
            }
        }
        binding.txtScore.text = "Your Score : $score"
    }

    companion object {
        fun newInstance(quizJson: String): ResultFragment {
            return ResultFragment().apply {
                arguments = Bundle().apply {
                    putString("QUIZ", quizJson)
                }
            }
        }
    }
}