package com.example.mymemory.models

data class MemoryCard(
    val id: Int,
    var isFaceUp: Boolean = false,
    var isMatched: Boolean = false,
)