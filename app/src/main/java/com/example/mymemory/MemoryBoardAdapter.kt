package com.example.mymemory

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.setMargins
import androidx.recyclerview.widget.RecyclerView
import com.example.mymemory.models.BoardSize
import com.example.mymemory.models.MemoryCard
import kotlin.math.min


class MemoryBoardAdapter(
    private val context: MainActivity,
    private val boardSize: BoardSize,
    private val cards: List<MemoryCard>,
    private val cardClickListener: ICardClickListener
) :
    RecyclerView.Adapter<MemoryBoardAdapter.ViewHolder>() {

    companion object {
        private const val MARGIN_SIZE = 10
        private const val TAG = "MemoryAdapter"
    }

    interface ICardClickListener {
        fun onCardClicked(position: Int)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ibBtn = itemView.findViewById<ImageButton>(R.id.ibBtn)

        fun bind(position: Int) {
            val card = cards[position]
            if (card.isFaceUp) {
                ibBtn.setImageResource(card.id)
            } else {
                ibBtn.setImageResource(R.drawable.ic_back)
            }

            ibBtn.alpha = if (card.isMatched) 0.4f else 1.0f
            val colorStateList = if (card.isMatched) ContextCompat.getColorStateList(
                context,
                R.color.custom_gray
            ) else null
            ViewCompat.setBackgroundTintList(ibBtn, colorStateList)

            ibBtn.setOnClickListener {
                Log.i(TAG, "Yay, clicked on $position!")
                cardClickListener.onCardClicked(position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MemoryBoardAdapter.ViewHolder {
        val cardWidth = parent.width / boardSize.getWidth() - MARGIN_SIZE * 2
        val cardHeight = parent.height / boardSize.getHeight() - MARGIN_SIZE * 2
        val sideSize = min(cardWidth, cardHeight)

        val view = LayoutInflater.from(context).inflate(R.layout.memory_card, parent, false)
        val cardView = view.findViewById<CardView>(R.id.cvCardView)
        val params = cardView.layoutParams as ViewGroup.MarginLayoutParams
        params.width = sideSize
        params.height = sideSize
        params.setMargins(MARGIN_SIZE)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: MemoryBoardAdapter.ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = boardSize.cardsNum
}
