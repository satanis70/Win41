package com.example.win41.model

import androidx.annotation.Keep

@Keep
data class Question(
    val img: String,
    val name: String,
    val nationality: String,
    val points: String
)