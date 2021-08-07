package com.example.mymemory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var rvBoard: RecyclerView
    private lateinit var tvScore: TextView
    private lateinit var tvMoves: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvBoard = findViewById(R.id.rvBoard)
        tvMoves = findViewById(R.id.tvMoves)
        tvScore = findViewById(R.id.tvScore)

        rvBoard.layoutManager = GridLayoutManager(this, 2)
        rvBoard.adapter = MemoryAdapter(this, 8)

        // Micro-optimisation. Adapter doesn't affect view size.
        rvBoard.setHasFixedSize(true)
    }
}