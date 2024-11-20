package com.example.moovie_book_tracker

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

object ksiazkasLubFilmsJsonManager {
    private const val FILE_NAME = "sweets_data.json"

    fun saveSweetsListToJson(
        context: Context,
        sweetsList: List<KsiazkaLubFilm>,
    ) {
        // val gson = Gson()
        val gson = GsonBuilder().setPrettyPrinting().create()
        val jsonString = gson.toJson(sweetsList)
        val file = File(context.filesDir, FILE_NAME)
        // filesDir to ścieżka, która w systemie Android jest zlokalizowana w pamięci wewnętrznej,
        // dostępnej tylko dla danej aplikacji.

        file.writeText(jsonString)
    }

    fun loadSweetsListFromJson(context: Context): List<KsiazkaLubFilm> {
        val file = File(context.filesDir, FILE_NAME)
        return if (file.exists()) {
            val jsonString = file.readText()
            Gson().fromJson(jsonString, Array<KsiazkaLubFilm>::class.java).toList()
        } else {
            emptyList()
        }
    }
}
