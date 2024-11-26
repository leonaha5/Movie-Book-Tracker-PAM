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
    var ksiazkaLubFilmList = mutableListOf<KsiazkaLubFilm>()
    var visibleKsiazkaLubFilmList = mutableListOf<KsiazkaLubFilm>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ksiazkaLubFilmList =
            KsiazkasLubFilmsJsonManager.loadSweetsListFromJson(this).toMutableList()

        val showAlertDialog: (String, String) -> Unit = { gatunek, pszeczytane ->
            AlertDialog
                .Builder(this)
                .setTitle("Jakiś tytuł")
                .setMessage("Gatunek: ${gatunek}\nPrzeczytane $pszeczytane")
                .setNeutralButton("Zamknij") { dialog, _ ->
                    dialog.dismiss()
                }.create()
                .show()
        }

        val delete: (Int) -> Unit = { position ->
            ksiazkaLubFilmList.removeAt(position)
            visibleKsiazkaLubFilmList.removeAt(position)
            KsiazkasLubFilmsJsonManager.saveSweetsListToJson(this, ksiazkaLubFilmList)
        }

        val adapter =
            Adapter(
                ksiazkaLubFilmList = visibleKsiazkaLubFilmList,
                showAlertDialog = showAlertDialog,
                delete = delete,
            )

        val recyclerView = findViewById<RecyclerView>(R.id.KsiazkaLubFilmRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val radioButtonAll = findViewById<RadioButton>(R.id.radioButtonAll)
        val radioButtonRead = findViewById<RadioButton>(R.id.radioButtonRead)
        val radioButtonNotRead = findViewById<RadioButton>(R.id.radioButtonNotRead)

        radioButtonAll.setOnClickListener {
            visibleKsiazkaLubFilmList.clear()
            visibleKsiazkaLubFilmList.addAll(ksiazkaLubFilmList) // Add all items back
            adapter.notifyDataSetChanged()
        }

        radioButtonRead.setOnClickListener {
            visibleKsiazkaLubFilmList.clear()
            visibleKsiazkaLubFilmList.addAll(ksiazkaLubFilmList.filter { it.pszeczytane })
            adapter.notifyDataSetChanged()
        }

        radioButtonNotRead.setOnClickListener {
            visibleKsiazkaLubFilmList.clear()
            visibleKsiazkaLubFilmList.addAll(ksiazkaLubFilmList.filter { !it.pszeczytane })
            adapter.notifyDataSetChanged()
        }

        val tytulEditText = findViewById<EditText>(R.id.tytulEditText)
        val editTextRecenzja = findViewById<EditText>(R.id.editTextRecenzja)
        val editTextGatunek = findViewById<EditText>(R.id.editTextGatunek)
        val seekBarOcena = findViewById<SeekBar>(R.id.seekBarOcena)
        val przeczytaneCheckBox = findViewById<CheckBox>(R.id.pszeczytaneCheckBox)

        findViewById<Button>(R.id.AddButton).setOnClickListener {
            val newKsiazka =
                KsiazkaLubFilm(
                    tytulEditText.text.toString().ifEmpty { "Brak tytulu" },
                    editTextRecenzja.text.toString().ifEmpty { "Brak recenzji" },
                    editTextGatunek.text.toString().ifEmpty { "Brak gatunku" },
                    seekBarOcena.progress,
                    przeczytaneCheckBox.isChecked,
                )

            ksiazkaLubFilmList.add(newKsiazka)
            visibleKsiazkaLubFilmList.add(newKsiazka)
            adapter.notifyItemInserted(visibleKsiazkaLubFilmList.size - 1)

            KsiazkasLubFilmsJsonManager.saveSweetsListToJson(this, visibleKsiazkaLubFilmList)

            tytulEditText.text.clear()
            editTextRecenzja.text.clear()
            editTextGatunek.text.clear()
            seekBarOcena.progress = 0
            przeczytaneCheckBox.isChecked = false
        }
    }
}
