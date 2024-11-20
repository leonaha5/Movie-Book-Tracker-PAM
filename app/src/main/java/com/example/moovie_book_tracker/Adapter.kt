package com.example.moovie_book_tracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Adapter(
    private val ksiazkaLubFilmList: List<KisazkaLubFilm>,
) : RecyclerView.Adapter<Adapter.KsiazkaLubFilmViewHolder>() {
    class KsiazkaLubFilmViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        val kisazkaLubFilmTytul: TextView = itemView.findViewById(R.id.klfLayoutTytulText)
        val kisazkaLubFilmRecenzja: TextView = itemView.findViewById(R.id.klfLayoutRecenzjaText)
        val kisazkaLubFilmGatunek: TextView = itemView.findViewById(R.id.klfLayoutGatunekText)
        val ksiaKisazkaLubFilmOcena: TextView = itemView.findViewById(R.id.klfLayoutOcenaText)
        val ksiaKisazkaLubFilmRodzaj: TextView = itemView.findViewById(R.id.klfLayoutRodzajText)
        val ksiaKisazkaLubFilmPszeczytane: TextView =
            itemView.findViewById(R.id.klfLayoutPszeczytaneText)
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
        holder.kisazkaLubFilmGatunek.text = "Gatunek: ${currentKsiazkaLubFilm.gatunek}"
        holder.ksiaKisazkaLubFilmOcena.text = "Ocena: ${currentKsiazkaLubFilm.ocena}"
        holder.ksiaKisazkaLubFilmRodzaj.text = "Rodzaj: ${currentKsiazkaLubFilm.rodzaj}"
        holder.ksiaKisazkaLubFilmPszeczytane.text =
            "Pszeczytane: ${if (currentKsiazkaLubFilm.pszeczytane) "Tak" else "Nie"}"
    }

    override fun getItemCount(): Int = ksiazkaLubFilmList.size
}
