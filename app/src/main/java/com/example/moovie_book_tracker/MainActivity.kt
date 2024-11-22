package com.example.moovie_book_tracker

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
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
    var ksiazkaLubFilmList = mutableListOf<KsiazkaLubFilm>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val tytulEditText = findViewById<EditText>(R.id.tytulEditText)
        val recenzjaEditText = findViewById<EditText>(R.id.recenzjaEditText)
        val gatunekEditText = findViewById<EditText>(R.id.gatunekEditText)
        val ocenaSeekBar = findViewById<SeekBar>(R.id.ocenaSeekBar)
        val rodzajRadioGroup = findViewById<RadioGroup>(R.id.rodzajRadioGroup)
        val przeczytaneCheckBox = findViewById<CheckBox>(R.id.pszeczytaneCheckBox)

        ksiazkaLubFilmList =
            KsiazkasLubFilmsJsonManager.loadSweetsListFromJson(this).toMutableList()

        val recyclerView = findViewById<RecyclerView>(R.id.KsiazkaLubFilmRecyclerView)

        val showAlertDialog: (String, String, String) -> Unit = { rodzaj, gatunek, pszeczytane ->
            AlertDialog
                .Builder(this)
                .setTitle("Jakiś tytuł")
                .setMessage("Rodzaj: ${rodzaj}\nGatunek: ${gatunek}\nPrzeczytane $pszeczytane")
                .setNeutralButton("Zamknij") { dialog, _ ->
                    dialog.dismiss()
                }.create()
                .show()
        }
        val delete: (Int) -> Unit = { position ->
            ksiazkaLubFilmList.removeAt(position)
        }

        val adapter =
            Adapter(
                ksiazkaLubFilmList = ksiazkaLubFilmList,
                showAlertDialog = showAlertDialog,
            )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.AddButton).setOnClickListener {
            lateinit var rodzaj: String
            val selectedRadioButtonId = rodzajRadioGroup.checkedRadioButtonId

            rodzaj =
                if (selectedRadioButtonId == -1) {
                    "Brak rodzaju"
                } else {
                    findViewById<RadioButton>(selectedRadioButtonId).text.toString()
                }

            ksiazkaLubFilmList.add(
                KsiazkaLubFilm(
                    tytulEditText.text.toString().ifEmpty { "Brak tytulu" },
                    recenzjaEditText.text.toString().ifEmpty { "Brak recenzji" },
                    gatunekEditText.text.toString().ifEmpty { "Brak gatunku" },
                    ocenaSeekBar.progress,
                    rodzaj,
                    przeczytaneCheckBox.isChecked,
                ),
            )

            try {
                KsiazkasLubFilmsJsonManager.saveSweetsListToJson(this, ksiazkaLubFilmList)
            } catch (ex: Exception) {
                Log.e("save", "Coś poszło nie tak $ex")
            }

            tytulEditText.text.clear()
            recenzjaEditText.text.clear()
            gatunekEditText.text.clear()
            rodzajRadioGroup.clearCheck()
            ocenaSeekBar.progress = 0
            przeczytaneCheckBox.isChecked = false

            adapter.notifyItemInserted(ksiazkaLubFilmList.size - 1)
        }
    }
}
