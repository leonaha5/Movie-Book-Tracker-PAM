package com.example.moovie_book_tracker

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private var itemsList = mutableListOf<Item>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        setupWindowInsets()
        loadItemsFromJson()
        initializeRecyclerView()
        initializeFilters()
        initializeAddItemButton()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun loadItemsFromJson() {
        itemsList =
            ItemsJsonManager.loadItemsFromJson(this).toMutableList()
    }

    private fun initializeRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.itemsRecyclerView)

        val showItemDetailsDialog: (String, String) -> Unit = { genre, readStatus ->
            AlertDialog
                .Builder(this)
                .setTitle("Details")
                .setMessage("Genre: $genre\nRead: $readStatus")
                .setNeutralButton("Close") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }

        val deleteItemFromList: (Int) -> Unit = { position ->
            itemsList.removeAt(position)
            ItemsJsonManager.saveItemsToJson(this, itemsList)
        }

        val adapter =
            Adapter(
                itemList = itemsList,
                showItemDetailsDialog = showItemDetailsDialog,
                deleteItemFromList = deleteItemFromList,
            )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun initializeFilters() {
        val radioButtonAll = findViewById<RadioButton>(R.id.radioButtonAll)
        val radioButtonRead = findViewById<RadioButton>(R.id.radioButtonRead)
        val radioButtonNotRead = findViewById<RadioButton>(R.id.radioButtonNotRead)
        val adapter =
            (findViewById<RecyclerView>(R.id.itemsRecyclerView).adapter as Adapter)

        radioButtonAll.setOnClickListener {
            itemsList.forEach { it.isVisible = true }
            adapter.notifyDataSetChanged()
        }

        radioButtonRead.setOnClickListener {
            itemsList.forEach { it.isVisible = it.isRead }
            adapter.notifyDataSetChanged()
        }

        radioButtonNotRead.setOnClickListener {
            itemsList.forEach { it.isVisible = !it.isRead }
            adapter.notifyDataSetChanged()
        }
    }

    private fun initializeAddItemButton() {
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val reviewEditText = findViewById<EditText>(R.id.reviewEditText)
        val genreEditText = findViewById<EditText>(R.id.genreEditText)
        val ratingSeekBar = findViewById<SeekBar>(R.id.ratingSeekBar)
        val readCheckBox = findViewById<CheckBox>(R.id.readCheckBox)
        val adapter =
            (findViewById<RecyclerView>(R.id.itemsRecyclerView).adapter as Adapter)

        findViewById<Button>(R.id.addItemButton).setOnClickListener {
            val newItem =
                Item(
                    title = titleEditText.text.toString().ifEmpty { "No Title" },
                    review = reviewEditText.text.toString().ifEmpty { "No Review" },
                    genre = genreEditText.text.toString().ifEmpty { "No Genre" },
                    rating = ratingSeekBar.progress,
                    isRead = readCheckBox.isChecked,
                    isVisible = true,
                )

            itemsList.add(newItem)
            ItemsJsonManager.saveItemsToJson(this, itemsList)

            titleEditText.text.clear()
            reviewEditText.text.clear()
            genreEditText.text.clear()
            ratingSeekBar.progress = 0
            readCheckBox.isChecked = false

            adapter.notifyItemInserted(itemsList.size - 1)
        }
    }
}
