package com.example.mymemory

import android.animation.ArgbEvaluator
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mymemory.models.BoardSize
import com.example.mymemory.models.MemoryGame
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var rvBoard: RecyclerView
    private lateinit var tvScore: TextView
    private lateinit var tvMoves: TextView
    private lateinit var clRoot: ConstraintLayout

    private lateinit var memoryGame: MemoryGame
    private lateinit var adapter: MemoryBoardAdapter

    private var boardSize: BoardSize = BoardSize.EASY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvBoard = findViewById(R.id.rvBoard)
        tvMoves = findViewById(R.id.tvMoves)
        tvScore = findViewById(R.id.tvScore)
        clRoot = findViewById(R.id.clRoot)

        initializeGame()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miRefresh -> {
                if (memoryGame.numMoves > 0 && !memoryGame.haveWon) {
                    showAlertDialog("Refresh? You will lose current progress", null, View.OnClickListener {
                        initializeGame()
                        updateTextes(true)
                    })
                }
                else {
                    initializeGame()
                    updateTextes(true)
                }
            }
            R.id.miNewActivity -> {
                val intent = Intent(this, SecondActivity::class.java)
                intent.putExtra("KEK", 15)
                startActivityForResult(intent, 42)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun showAlertDialog(
        title: String,
        view: View?,
        positiveClickListener: View.OnClickListener
    ) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK") { _, _ ->
                positiveClickListener.onClick(null)
            }.show()
    }

    private fun initializeGame() {
        memoryGame = MemoryGame(boardSize)

        val clickListener = object : MemoryBoardAdapter.ICardClickListener {
            override fun onCardClicked(position: Int) {
                updateGameOnClick(position)
            }
        }
        adapter = MemoryBoardAdapter(this, boardSize, memoryGame.cards, clickListener)
        rvBoard.adapter = adapter
        rvBoard.layoutManager = GridLayoutManager(this, boardSize.getWidth())

        // Micro-optimisation. Adapter doesn't affect view size.
        rvBoard.setHasFixedSize(true)
        tvScore.text = getString(R.string.score, 0, boardSize.getNumPairs())
        tvMoves.text = getString(R.string.moves, 0)

        tvScore.setTextColor(ContextCompat.getColor(this, R.color.progress_start))
    }

    private fun updateGameOnClick(pos: Int) {
        if (memoryGame.haveWon) {
            Snackbar.make(clRoot, "You already won!", Snackbar.LENGTH_LONG).show()
            return
        }

        if (memoryGame.isFlipped(pos)) {
            Snackbar.make(clRoot, "Nope.", Snackbar.LENGTH_SHORT).show()
            return
        }

        val isMatched = memoryGame.cardSelected(pos)
        updateTextes(isMatched)
        memoryGame.lastPositions.forEach { adapter.notifyItemChanged(it) }
    }

    private fun updateTextes(updateMatched: Boolean = false) {
        if (updateMatched) {
            tvScore.text =
                getString(R.string.score, memoryGame.numPairsCount, boardSize.getNumPairs())
            val color = ArgbEvaluator().evaluate(
                memoryGame.numPairsCount.toFloat() / boardSize.getNumPairs(),
                ContextCompat.getColor(this, R.color.progress_start),
                ContextCompat.getColor(this, R.color.progress_end)
            ) as Int
            tvScore.setTextColor(color)
        }

        tvMoves.text = getString(R.string.moves, memoryGame.numMoves)
    }
}