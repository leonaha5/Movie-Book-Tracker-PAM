package com.example.moovie_book_tracker

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

object ItemsJsonManager {
    private const val FILE_NAME = "sweets_data.json"

    fun saveItemsToJson(
        context: Context,
        itemsList: List<Item>,
    ) {
        File(context.filesDir, FILE_NAME).writeText(
            GsonBuilder().setPrettyPrinting().create().toJson(itemsList),
        )
    }

    fun loadItemsFromJson(context: Context): List<Item> {
        val file = File(context.filesDir, FILE_NAME)
        return if (file.exists()) {
            Gson().fromJson(file.readText(), Array<Item>::class.java).toList()
        } else {
            emptyList()
        }
    }
}
