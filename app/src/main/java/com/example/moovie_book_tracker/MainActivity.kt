package com.example.moovie_book_tracker

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.SeekBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val listOfKsiazkasOrFilms = mutableListOf<KisazkaLubFilm>()

        val recyclerView = findViewById<RecyclerView>(R.id.KsiazkaLubFilmRecyclerView)

        val addButton = findViewById<Button>(R.id.AddButton)
        val tytulEditText = findViewById<EditText>(R.id.tytulEditText)
        val recenzjaEditText = findViewById<EditText>(R.id.recenzjaEditText)
        val gatunekEditText = findViewById<EditText>(R.id.gatunekEditText)
        val ocenaSeekBar = findViewById<SeekBar>(R.id.ocenaSeekBar)
        val rodzajRadioGroup = findViewById<RadioGroup>(R.id.rodzajRadioGroup)
        val przeczytaneCheckBox = findViewById<CheckBox>(R.id.pszeczytaneCheckBox)

        addButton.setOnClickListener {
            lateinit var rodzaj: String

            val selectedRadioButtonId = rodzajRadioGroup.checkedRadioButtonId
            if (selectedRadioButtonId == -1) {
                rodzaj = "Brak rodzaju"
            } else {
                rodzaj = findViewById<RadioButton>(selectedRadioButtonId).text.toString()
            }
            tytulEditText.text.clear()
            recenzjaEditText.text.clear()
            gatunekEditText.text.clear()
            rodzajRadioGroup.clearCheck()
            ocenaSeekBar.progress = 0

            listOfKsiazkasOrFilms.add(
                KisazkaLubFilm(
                    tytulEditText.text.toString().ifEmpty { "Brak tytulu" },
                    recenzjaEditText.text.toString().ifEmpty { "Brak recenzji" },
                    gatunekEditText.text.toString().ifEmpty { "Brak gatunku" },
                    ocenaSeekBar.progress,
                    rodzaj,
                    przeczytaneCheckBox.isChecked,
                ),
            )
            recyclerView.adapter?.notifyItemInserted(listOfKsiazkasOrFilms.size - 1)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Adapter(listOfKsiazkasOrFilms)
    }
}
