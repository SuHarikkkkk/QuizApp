package com.example.quizapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizapp.R
import com.example.quizapp.fragments.ResultFragment
import com.example.quizapp.adapters.OptionAdapter
import com.example.quizapp.databinding.FragmentNeMainBinding
import com.example.quizapp.databinding.FragmentQuestionBinding
import com.example.quizapp.databinding.FragmentSignupBinding
import com.example.quizapp.models.Question
import com.example.quizapp.models.Quiz
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class QuestionFragment : Fragment() {

    private var quizzes: MutableList<Quiz>? = null
    private var questions: MutableMap<String, Question>? = null
    private var index = 1
    private lateinit var binding: FragmentQuestionBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpFirestore()
        setUpEventListener()
    }

    private fun setUpEventListener() {
        binding.btnPrevious.setOnClickListener {
            index--
            bindViews()
        }

        binding.btnNext.setOnClickListener {
            index++
            bindViews()
        }

        binding.btnSubmit.setOnClickListener {
            Log.d("FINALQUIZ", questions.toString())

            val json = Gson().toJson(quizzes!![0])
//            val action = QuestionFragmentDirections.actionQuestionFragmentToResultFragment(json)
            findNavController().navigate(R.id.action_questionFragment_to_resultFragment)
        }
    }

    private fun setUpFirestore() {
        val firestore = FirebaseFirestore.getInstance()
        val date = arguments?.getString("DATE")
        if (date != null) {
            firestore.collection("quizzes").whereEqualTo("title", date)
                .get()
                .addOnSuccessListener {
                    if (it != null && !it.isEmpty) {
                        quizzes = it.toObjects(Quiz::class.java)
                        questions = quizzes!![0].questions
                        bindViews()
                    }
                }
        }
    }

    private fun bindViews() {
        binding.btnPrevious.visibility = View.GONE
        binding.btnSubmit.visibility = View.GONE
        binding.btnNext.visibility = View.GONE

        when {
            index == 1 -> {
                binding.btnNext.visibility = View.VISIBLE
            }
            index == questions!!.size -> {
                binding.btnSubmit.visibility = View.VISIBLE
                binding.btnPrevious.visibility = View.VISIBLE
            }
            else -> {
                binding.btnPrevious.visibility = View.VISIBLE
                binding.btnNext.visibility = View.VISIBLE
            }
        }

        val question = questions!!["question$index"]
        question?.let {
            binding.description.text = it.description
            val optionAdapter = OptionAdapter(requireContext(), it)
            binding.optionList.layoutManager = LinearLayoutManager(requireContext())
            binding.optionList.adapter = optionAdapter
            binding.optionList.setHasFixedSize(true)
        }
    }

    companion object {
        fun newInstance(date: String): QuestionFragment {
            val fragment = QuestionFragment()
            val args = Bundle()
            args.putString("DATE", date)
            fragment.arguments = args
            return fragment
        }
    }
}