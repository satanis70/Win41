package com.example.win41.model

import androidx.annotation.Keep

@Keep
data class PostModel(
    val phoneName: String,
    val locale: String,
    val unique: String
)