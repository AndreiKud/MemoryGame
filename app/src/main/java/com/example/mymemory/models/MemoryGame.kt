package com.example.mymemory.models

import com.example.mymemory.utils.DEFAULT_ICONS

class MemoryGame(
    private val boardSize: BoardSize
) {
    private var selectedCardIndex: Int? = null

    var numMoves: Int = 0
        private set

    var numPairsCount: Int = 0
        private set

    var cards: List<MemoryCard>
        private set

    val lastPositions = mutableListOf<Int>()

    init {
        val chosenCards = DEFAULT_ICONS.shuffled().take(boardSize.getNumPairs())
        val pairedCards = (chosenCards + chosenCards).shuffled()
        cards = pairedCards.map { MemoryCard(it) }
    }

    companion object {
        private const val MAX_SELECTED_COUNT = 3
    }

    val haveWon: Boolean
        get() = numPairsCount == cards.size / 2

    fun cardSelected(pos: Int): Boolean {
        var isMatched = false

        lastPositions.add(pos)
        if (lastPositions.size > MAX_SELECTED_COUNT)
            lastPositions.removeFirst()

        if (selectedCardIndex == null) {
            restoreCards()
            selectedCardIndex = pos
        } else {
            if (checkMatch(selectedCardIndex!!, pos)) {
                cards[selectedCardIndex!!].isMatched = true
                cards[pos].isMatched = true
                numPairsCount++
                isMatched = true
            }

            selectedCardIndex = null
        }

        numMoves++
        cards[pos].isFaceUp = true
        return isMatched
    }

    private fun checkMatch(pos1: Int, pos2: Int): Boolean {
        return cards[pos1].id == cards[pos2].id
    }

    private fun restoreCards() {
        cards.forEach { if (!it.isMatched) it.isFaceUp = false }
    }

    fun isFlipped(pos: Int): Boolean {
        return cards[pos].isFaceUp
    }
}