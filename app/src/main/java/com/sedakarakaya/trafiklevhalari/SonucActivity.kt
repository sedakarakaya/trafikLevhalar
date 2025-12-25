package com.sedakarakaya.trafiklevhalari

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SonucActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sonuc)

        // 1. Verileri Al
        val gelenDogruSayisi = intent.getIntExtra("DOGRU_SAYISI", 0)
        val gelenYanlisSayisi = intent.getIntExtra("YANLIS_SAYISI", 0)
        val gelenKategori = intent.getStringExtra("SECILEN_KATEGORI")

        // 2. Görsel Elemanları Tanımla
        val txtPuan = findViewById<TextView>(R.id.txtPuan) // YENİ
        val txtDogru = findViewById<TextView>(R.id.txtDogruSonuc)
        val txtYanlis = findViewById<TextView>(R.id.txtYanlisSonuc)
        val btnTekrar = findViewById<Button>(R.id.btnTekrarDene)
        val btnKategoriler = findViewById<Button>(R.id.btnKategorilereDon)

        // 3. Puanı Hesapla (Her soru 2 puan)
        val toplamPuan = gelenDogruSayisi * 2

        // 4. Verileri Ekrana Yazdır
        txtPuan.text = "PUAN: $toplamPuan"
        txtDogru.text = "Doğru Sayısı: $gelenDogruSayisi"
        txtYanlis.text = "Yanlış Sayısı: $gelenYanlisSayisi"

        // 5. Buton İşlemleri
        btnTekrar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("SECILEN_KATEGORI", gelenKategori)
            startActivity(intent)
            finish()
        }

        btnKategoriler.setOnClickListener {
            val intent = Intent(this, KategorilerActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}