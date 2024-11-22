package com.example.moovie_book_tracker

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

object KsiazkasLubFilmsJsonManager {
    private const val FILE_NAME = "sweets_data.json"

    fun saveSweetsListToJson(
        context: Context,
        sweetsList: List<KsiazkaLubFilm>,
    ) {
        File(context.filesDir, FILE_NAME).writeText(
            GsonBuilder().setPrettyPrinting().create().toJson(sweetsList),
        )
    }

    fun loadSweetsListFromJson(context: Context): List<KsiazkaLubFilm> {
        val file = File(context.filesDir, FILE_NAME)
        return if (file.exists()) {
            Gson().fromJson(file.readText(), Array<KsiazkaLubFilm>::class.java).toList()
        } else {
            emptyList()
        }
    }
}
