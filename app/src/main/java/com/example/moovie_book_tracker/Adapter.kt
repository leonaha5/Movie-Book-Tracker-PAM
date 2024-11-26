package com.example.moovie_book_tracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(
    private val ksiazkaLubFilmList: MutableList<KsiazkaLubFilm>,
    private val showAlertDialog: (String, String) -> Unit,
    private val delete: (Int) -> Unit,
) : RecyclerView.Adapter<Adapter.KsiazkaLubFilmViewHolder>() {
    class KsiazkaLubFilmViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        val kisazkaLubFilmTytul: TextView = itemView.findViewById(R.id.layoutTytul)
        val kisazkaLubFilmRecenzja: TextView = itemView.findViewById(R.id.layoutRecenzja)
        val ksiaKisazkaLubFilmOcena: TextView = itemView.findViewById(R.id.layoutOcena)
        val deleteButton: TextView = itemView.findViewById(R.id.deleteButton)
        val innerLayout = itemView.findViewById<LinearLayout>(R.id.innerLayout)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): KsiazkaLubFilmViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.ksiazka_lub_film, parent, false)

        return KsiazkaLubFilmViewHolder(itemView)
    }

    override fun onBindViewHolder(
        holder: KsiazkaLubFilmViewHolder,
        position: Int,
    ) {
        val currentKsiazkaLubFilm = ksiazkaLubFilmList[position]

        holder.kisazkaLubFilmTytul.text = currentKsiazkaLubFilm.tytul
        holder.kisazkaLubFilmRecenzja.text = currentKsiazkaLubFilm.recenzja
        holder.ksiaKisazkaLubFilmOcena.text = "Ocena: ${currentKsiazkaLubFilm.ocena}"

        val pszeczytane = if (currentKsiazkaLubFilm.pszeczytane) "Tak" else "Nie"

        holder.innerLayout.setOnClickListener {
            showAlertDialog(
                currentKsiazkaLubFilm.gatunek,
                pszeczytane,
            )
        }

        holder.deleteButton.setOnLongClickListener {
            delete(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, ksiazkaLubFilmList.size)
            true
        }
    }

    override fun getItemCount(): Int = ksiazkaLubFilmList.size
}
