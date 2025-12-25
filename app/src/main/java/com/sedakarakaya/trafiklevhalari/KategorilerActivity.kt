package com.sedakarakaya.trafiklevhalari

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class KategorilerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kategoriler)

        val btn1 = findViewById<Button>(R.id.btnKategori1)
        val btn2 = findViewById<Button>(R.id.btnKategori2)
        val btn3 = findViewById<Button>(R.id.btnKategori3)
        val btn4 = findViewById<Button>(R.id.btnKategori4)
        val btn5 = findViewById<Button>(R.id.btnKategori5)
        val btn6 = findViewById<Button>(R.id.btnKategori6)

        btn1.setOnClickListener { oyunuBaslat("kategori_1") }
        btn2.setOnClickListener { oyunuBaslat("kategori_2") }
        btn3.setOnClickListener { oyunuBaslat("kategori_3") }
        btn4.setOnClickListener { oyunuBaslat("kategori_4") }
        btn5.setOnClickListener { oyunuBaslat("kategori_5") }
        btn6.setOnClickListener { oyunuBaslat("kategori_6") }
    }
    private fun oyunuBaslat(kategoriKodu: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("SECILEN_KATEGORI", kategoriKodu)
        startActivity(intent)
    }
}



