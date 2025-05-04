package com.example.quizapp.models

data class Quiz(
    var id: Int,
    var title: String = "",
    var questions: MutableMap<String, Question> = mutableMapOf()
)