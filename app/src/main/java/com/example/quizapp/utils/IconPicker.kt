package com.example.quizapp.utils

import com.example.quizapp.R

object IconPicker {
    val icons = arrayOf(
        R.drawable.bell_vector,
        R.drawable.calculator_vector,
        R.drawable.education_board_vector,
        R.drawable.line_building_vector,
        R.drawable.pencil_vector,
        R.drawable.ruler_vector,
        R.drawable.school_bag_vector,
        R.drawable.write_letter_vector
    )
    var currentIcon = 0

    fun getIcon(): Int {
        currentIcon = (currentIcon + 1) % icons.size
        return icons[currentIcon]
    }
}
