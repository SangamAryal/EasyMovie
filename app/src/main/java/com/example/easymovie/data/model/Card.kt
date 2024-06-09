package com.example.easymovie.data.model

data class Card(
    val type: String,
    val title: String,
    val localImageResource: String
)

data class CardRow(
    val title: String,
    val cards: List<Card>
)
