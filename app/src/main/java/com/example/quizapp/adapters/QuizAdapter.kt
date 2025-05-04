package com.example.quizapp.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.cheezycode.quizzed.utils.ColorPicker
import com.example.quizapp.R
import com.example.quizapp.databinding.FragmentLoginBinding
import com.example.quizapp.fragments.QuestionFragment
import com.example.quizapp.models.Quiz
import com.example.quizapp.utils.IconPicker


private lateinit var binding: QuestionFragment
class QuizAdapter(val context: Context, val quizzes: List<Quiz>) :
    RecyclerView.Adapter<QuizAdapter.QuizViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuizViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.quiz_item, parent, false)
        return QuizViewHolder(view)
    }

    override fun onBindViewHolder(holder: QuizViewHolder, position: Int) {
        holder.textViewTitle.text = quizzes[position].title
        holder.cardContainer.setCardBackgroundColor(Color.parseColor(ColorPicker.getColor()))
        holder.iconView.setImageResource(IconPicker.getIcon())
        holder.itemView.setOnClickListener {
            Toast.makeText(context, quizzes[position].title, Toast.LENGTH_SHORT).show()
            // Используем Navigation Component
            val navController = holder.itemView.findNavController()
            navController.navigate(
                R.id.action_neMainFragment_to_questionFragment,
                bundleOf("QUIZ_TITLE" to quizzes[position].title)  // Передача данных
            )
        }
    }

    override fun getItemCount(): Int {
        return quizzes.size
    }

    inner class QuizViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textViewTitle: TextView = itemView.findViewById(R.id.quizTitle)
        var iconView: ImageView = itemView.findViewById(R.id.quizIcon)
        var cardContainer: CardView = itemView.findViewById(R.id.cardContainer)
    }
}