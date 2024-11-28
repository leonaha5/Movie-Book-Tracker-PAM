package com.example.moovie_book_tracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(
    private val itemList: MutableList<Item>,
    private val showItemDetailsDialog: (String, String) -> Unit,
    private val deleteItemFromList: (Int) -> Unit,
) : RecyclerView.Adapter<Adapter.ItemViewHolder>() {
    class ItemViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.titleTextView)
        val reviewTextView: TextView = itemView.findViewById(R.id.reviewTextView)
        val ratingTextView: TextView = itemView.findViewById(R.id.ratingTextView)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
        val layout: LinearLayout = itemView.findViewById(R.id.layout)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ItemViewHolder {
        val itemView =
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.ksiazka_lub_film, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: ItemViewHolder,
        position: Int,
    ) {
        val currentKsiazkaLubFilm = itemList[position]

        holder.layout.visibility =
            if (currentKsiazkaLubFilm.isVisible) View.VISIBLE else View.GONE

        holder.titleTextView.text = currentKsiazkaLubFilm.title
        holder.reviewTextView.text = currentKsiazkaLubFilm.review
        holder.ratingTextView.text = "Ocena: ${currentKsiazkaLubFilm.rating}"

        val pszeczytane = if (currentKsiazkaLubFilm.isRead) "Tak" else "Nie"

        holder.layout.setOnLongClickListener {
            showItemDetailsDialog(currentKsiazkaLubFilm.genre, pszeczytane)
            true
        }

        holder.deleteButton.setOnClickListener {
            deleteItemFromList(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemList.size)
        }
    }

    override fun getItemCount(): Int = itemList.size
}
