package com.sedakarakaya.trafiklevhalari

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var imgLevha: ImageView
    private lateinit var txtSoruSayisi: TextView
    private lateinit var txtSure: TextView
    private lateinit var butonlar: List<Button>

    private lateinit var btnGeri: Button
    private lateinit var btnIleri: Button

    private val soruListesi = ArrayList<Soru>()
    private var simdikiSoruIndex = 0

    private var dogruSayisi = 0
    private var yanlisSayisi = 0
    private var secilenKategori: String? = null

    private val cevaplananSorular = HashSet<Int>()

    private var geriSayimSayaci: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        secilenKategori = intent.getStringExtra("SECILEN_KATEGORI")

        imgLevha = findViewById(R.id.imgLevha)
        txtSoruSayisi = findViewById(R.id.txtSoruSayisi)
        txtSure = findViewById(R.id.txtSure)

        val btn1 = findViewById<Button>(R.id.btnSecenek1)
        val btn2 = findViewById<Button>(R.id.btnSecenek2)
        val btn3 = findViewById<Button>(R.id.btnSecenek3)
        val btn4 = findViewById<Button>(R.id.btnSecenek4)
        butonlar = listOf(btn1, btn2, btn3, btn4)

        btnGeri = findViewById(R.id.btnGeri)
        btnIleri = findViewById(R.id.btnIleri)

        sorulariYukle()

        if (secilenKategori == "kategori_6") {
            sureyiBaslat(30 * 60 * 1000)
        } else {
            txtSure.visibility = View.GONE
        }

        if (soruListesi.isNotEmpty()) {
            soruyuEkranaBas()
        }

        for (i in 0 until butonlar.size) {
            butonlar[i].setOnClickListener {
                cevabiKontrolEt(i)
            }
        }
        btnGeri.setOnClickListener {
            if (simdikiSoruIndex > 0) {
                simdikiSoruIndex--
                soruyuEkranaBas()
            } else {
                Toast.makeText(this, "İlk sorudasınız", Toast.LENGTH_SHORT).show()
            }
        }
        btnIleri.setOnClickListener {
            if (simdikiSoruIndex < soruListesi.size - 1) {
                simdikiSoruIndex++
                soruyuEkranaBas()
            } else {
                Toast.makeText(this, "Son sorudasınız, lütfen cevaplayın", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun sureyiBaslat(toplamSureMs: Long) {
        geriSayimSayaci = object : CountDownTimer(toplamSureMs, 1000) {
            override fun onTick(kalanSureMs: Long) {
                val dakika = kalanSureMs / 1000 / 60
                val saniye = (kalanSureMs / 1000) % 60
                txtSure.text = String.format("%02d:%02d", dakika, saniye)
            }

            override fun onFinish() {
                txtSure.text = "00:00"
                Toast.makeText(applicationContext, "Süre Doldu!", Toast.LENGTH_LONG).show()
                sonucEkraniGoster()
            }
        }.start()
    }
    private fun sorulariYukle() {
        soruListesi.clear()
        when (secilenKategori) {
            "kategori_1" -> {
                soruListesi.add(Soru(R.drawable.levha1, listOf("A) Sola Tehlikeli Viraj", "B) Sağa Tehlikeli Viraj", "C) Kasisli Yol", "D) Yol Ver"), 1))
                soruListesi.add(Soru(R.drawable.levha2, listOf("A) Sola Tehlikeli Devamlı Virajlar", "B) Gevşek Şev", "C) Soldan Anayol Girişi", "D) Sola Tehlikeli Viraj"), 3))
                soruListesi.add(Soru(R.drawable.levha3, listOf("A) Sağa Tehlikeli Devamlı Virajlar", "B) Sola Tehlikeli Devamlı Virajlar", "C) İki Taraftan Daralan Kaplama", "D) Sağa Tehlikeli Viraj"), 0))
                soruListesi.add(Soru(R.drawable.levha4, listOf("A) Soldan Anayol Girişi", "B) Kontrolsüz Kavşak", "C) Sola Tehlikeli Devamlı Virajlar", "D) Dikkat"), 2))
                soruListesi.add(Soru(R.drawable.levha5, listOf("A) İki Yönlü Trafik", "B) Tehlikeli Eğim Çıkış", "C) Tehlikeli Eğim İniş", "D) Yandan Rüzgar"), 2))
                soruListesi.add(Soru(R.drawable.levha6, listOf("A) Gevşek Şev", "B) Tehlikeli Eğim Çıkış", "C) Yol Ver", "D) Tehlikeli Eğim İniş"), 1))
                soruListesi.add(Soru(R.drawable.levha7, listOf("A) İki Taraftan Daralan Kaplama", "B) Soldan Daralan Kaplama", "C) Sağdan Daralan Kaplama", "D) Yol Ver"), 0))
                soruListesi.add(Soru(R.drawable.levha8, listOf("A) Soldan Daralan Kaplama", "B) Sağdan Daralan Kaplama", "C) Yol Ver", "D) İki Taraftan Daralan Kaplama"), 1))
                soruListesi.add(Soru(R.drawable.levha9, listOf("A) İki Taraftan Daralan Kaplama", "B) Okul Geçidi", "C) Sağdan Daralan Kaplama", "D) Soldan Daralan Kaplama"), 3))
                soruListesi.add(Soru(R.drawable.levha10, listOf("A) Açılan Köprü", "B) İki Yönlü Trafik", "C) Deniz-Nehir Kıyısında Biten Yol", "D) Kasisli Yol"), 0))
                soruListesi.add(Soru(R.drawable.levha11, listOf("A) Açılan Köprü", "B) İki Yönlü Trafik", "C) Dikkat", "D) Deniz-Nehir Kıyısında Biten Yol"), 3))
                soruListesi.add(Soru(R.drawable.levha12, listOf("A) Gevşek Şev", "B) Kasisli Yol", "C) Deniz-Nehir Kıyısında Biten Yol", "D) Kaygan Yol"), 1))
                soruListesi.add(Soru(R.drawable.levha13, listOf("A) Kaygan Yol", "B) İki Yönlü Trafik", "C) Kasisli Yol", "D) Yaya Geçidi"), 0))
                soruListesi.add(Soru(R.drawable.levha14, listOf("A) Dikkat", "B) Gevşek Şev", "C) Gevşek Malzemeli Zemin", "D) Kasisli Yol"), 2))
                soruListesi.add(Soru(R.drawable.levha15, listOf("A) Yol Ver", "B) Gevşek Şev", "C) Deniz-Nehir Kıyısında Biten Yol", "D) Dikkat"), 1))
                soruListesi.add(Soru(R.drawable.levha16, listOf("A) Bisiklet Geçebilir", "B) Yol Ver", "C) Okul Geçidi", "D) Yaya Geçidi"), 3))
                soruListesi.add(Soru(R.drawable.levha17, listOf("A) Okul Geçidi", "B) Yaya Geçidi", "C) Yolda Çalışma", "D) Kasisli Yol"), 0))
                soruListesi.add(Soru(R.drawable.levha18, listOf("A) Yolda Çalışma", "B) Bisiklet Geçemez", "C) Bisiklet Geçebilir", "D) Yandan Rüzgar"), 2))
                soruListesi.add(Soru(R.drawable.levha19, listOf("A) Vahşi Hayvanlar", "B) Ehli Hayvanlar", "C) Dikkat", "D) Kasisli Yol"), 1))
                soruListesi.add(Soru(R.drawable.levha20, listOf("A) Yol ver", "B) İki Yönlü Trafik", "C) Ehli Hayvanlar", "D) Vahşi Hayvanlar"), 3))
                soruListesi.add(Soru(R.drawable.levha21, listOf("A) Okul Geçidi", "B) Yaya Geçidi", "C) Yolda Çalışma", "D) Kasisli Yol"), 2))
                soruListesi.add(Soru(R.drawable.levha22, listOf("A) Işıklı İşaret Cihazı", "B) İki Yönlü Trafik", "C) Yol Ver", "D) Düşük Banket"), 0))
                soruListesi.add(Soru(R.drawable.levha23, listOf("A) Yandan Rüzgar", "B) Havalimanı(Alçak Uçuş)", "C) Deniz-Nehir Kıyısında Biten Yol", "D) Kasisli Yol"), 1))
                soruListesi.add(Soru(R.drawable.levha24, listOf("A) Açılan Köprü", "B) Kontrolsüz Kavşak", "C) Dikkat", "D) Yandan Rüzgar"), 3))
                soruListesi.add(Soru(R.drawable.levha25, listOf("A) Yol Ver", "B) İki Yönlü Trafik", "C) AnaYol Tali Yol Kavşağı", "D) Kasisli Yol"), 1))
                soruListesi.add(Soru(R.drawable.levha26, listOf("A) Dikkat", "B) Yolda Çalışma", "C) Yol Ver", "D) Kasisli Yol"), 0))
                soruListesi.add(Soru(R.drawable.levha27, listOf("A) AnaYol Tali Yol Kavşağı", "B) İki Yönlü Trafik", "C) Kontrolsüz Kavşak", "D) Kasisli Yol"), 2))
                soruListesi.add(Soru(R.drawable.levha28, listOf("A) AnaYol Tali Yol Kavşağı", "B) İki Yönlü Trafik", "C) Yolda Çalışma", "D) Kasisli Yol"), 0))
                soruListesi.add(Soru(R.drawable.levha29, listOf("A) Kasisli Yol", "B) Sağdan Daralan Kaplama", "C) AnaYol Tali Yol Kavşağı", "D) Yol Ver"), 2))
                soruListesi.add(Soru(R.drawable.levha30, listOf("A) Yol Ver", "B) Soldan Daralan Kaplama", "C) Deniz-Nehir Kıyısında Biten Yol", "D) AnaYol Tali Yol Kavşağı"), 3))
                soruListesi.add(Soru(R.drawable.levha31, listOf("A) Tehlikeli Eğim Çıkış", "B) İki Yönlü Trafik", "C) AnaYol Tali Yol Kavşağı", "D) Dikkat"), 2))
                soruListesi.add(Soru(R.drawable.levha32, listOf("A) AnaYol Tali Yol Kavşağı", "B) İki Yönlü Trafik", "C) Sola Tehlikeli Devamlı Virajlar", "D) Tehlikeli Eğim İniş"), 0))
                soruListesi.add(Soru(R.drawable.levha33, listOf("A) Soldan Anayol Girişi", "B) Sağdan AnaYol Girişi", "C) Kontrollü Demiryolu Geçidi", "D) Yaya Geçidi"), 1))
                soruListesi.add(Soru(R.drawable.levha34, listOf("A) Soldan Anayol Girişi", "B) Sağdan AnaYol Girişi", "C) Dönel Kavşak", "D) Yolda Çalışma"), 0))
                soruListesi.add(Soru(R.drawable.levha35, listOf("A) Kontrollü Demiryolu Geçidi", "B) İki Yönlü Trafik", "C) Dönel Kavşak", "D) Yol Ver"), 2))
                soruListesi.add(Soru(R.drawable.levha36, listOf("A) Kontrolsüz Demiryolu Geçidi", "B) İki Yönlü Trafik", "C) Deniz-Nehir Kıyısında Biten Yol", "D) Kontrollü Demiryolu Geçidi"), 3))
                soruListesi.add(Soru(R.drawable.levha37, listOf("A) Kontrollü Demiryolu Geçidi", "B) Kontrolsüz Demiryolu Geçidi", "C) Soldan Anayol Girişi", "D) Dönel Kavşak"), 1))
                soruListesi.add(Soru(R.drawable.levha38, listOf("A) Yandan Rüzgar", "B) Kontrolsüz Demiryolu Geçidi(En Az İki Hat)", "C) Kontrolsüz Demiryolu Geçidi(Tek Hat)", "D) Kontrolsüz Kavşak"), 2))
                soruListesi.add(Soru(R.drawable.levha39, listOf("A) Dikkat", "B) Kontrolsüz Demiryolu Geçidi(En Az İki Hat)", "C) Yolda Çalışma", "D) Kontrolsüz Demiryolu Geçidi(Tek Hat)"), 1))
                soruListesi.add(Soru(R.drawable.levha40, listOf("A) Demiryolu Hemzemin Geçit Yaklaşımı(Sağ-Sol)", "B) Kontrolsüz Demiryolu Geçidi(En Az İki Hat)", "C) Deniz-Nehir Kıyısında Biten Yol", "D) Kontrolsüz Demiryolu Geçidi(Tek Hat)"), 0))
                soruListesi.add(Soru(R.drawable.levha41, listOf("A) Kontrolsüz Demiryolu Geçidi(En Az İki Hat)", "B) İki Yönlü Trafik", "C) Demiryolu Hemzemin Geçit Yaklaşımı(Sağ-Sol)", "D) Kasisli Yol"), 2))
                soruListesi.add(Soru(R.drawable.levha42, listOf("A) Köprü Başı Levhası(Sağ-Sol)", "B) Yolda Çalışma", "C) Deniz-Nehir Kıyısında Biten Yol", "D) Demiryolu Hemzemin Geçit Yaklaşımı(Sağ-Sol)"), 3))
                soruListesi.add(Soru(R.drawable.levha145, listOf("A) Köprü Başı Levhası(Sağ-Sol)", "B) Yolda Çalışma", "C) Deniz-Nehir Kıyısında Biten Yol", "D) Demiryolu Hemzemin Geçit Yaklaşımı(Sağ-Sol)"), 0))
                soruListesi.add(Soru(R.drawable.levha43, listOf("A) Düşük Banket", "B) Tehlikeli Viraj Yön Levhası", "C) Dönüş Adası Ek Levhası", "D) Gizli Buzlanma"), 1))
                soruListesi.add(Soru(R.drawable.levha44, listOf("A) Tehlikeli Viraj Yön Levhası", "B) İki Yönlü Trafik", "C) Yolda Çalışma", "D) Dönel Kavşak"), 0))
                soruListesi.add(Soru(R.drawable.levha45, listOf("A) Dönüş Adası Ek Levhası", "B) Düşük Banket", "C) Refüj Başı Ek Levhaları", "D) Trafik Sıkışıklığı"), 2))
                soruListesi.add(Soru(R.drawable.levha46, listOf("A) Dönüş Adası Ek Levhası", "B) İki Yönlü Trafik", "C) Yolda Çalışma", "D) Düşük Banket"), 0))
                soruListesi.add(Soru(R.drawable.levha47, listOf("A) Açılan Köprü", "B) İki Yönlü Trafik", "C) Yolda Çalışma", "D) Düşük Banket"), 3))
                soruListesi.add(Soru(R.drawable.levha48, listOf("A) Dönel Kavşak", "B) Dur", "C) Nehir Kıyısında Biten Yol", "D) Gizli Buzlanma"), 3))
                soruListesi.add(Soru(R.drawable.levha49, listOf("A) Yolda Çalışma", "B) Trafik Sıkışıklığı", "C) Dikkat", "D) Kasisli Yol"), 1))
                soruListesi.add(Soru(R.drawable.levha50, listOf("A) Gizli Buzlanma", "B) İki Yönlü Trafik", "C) Tramvay Hattı İle Oluşan Kavşak", "D) Kontrolsüz Demiryolu Geçidi"), 2))
            }
            "kategori_2" -> {
                soruListesi.add(Soru(R.drawable.levha51, listOf("A) Girişi Olmayan Yol", "B) Dur", "C) Park Yapılmaz", "D) Yol Ver"), 3))
                soruListesi.add(Soru(R.drawable.levha52, listOf("A) Girişi Olmayan Yol", "B) Dur", "C) Park Yapılmaz", "D) Yol Ver"), 1))
                soruListesi.add(Soru(R.drawable.levha53, listOf("A) Karşıdan Gelene Yol Ver", "B) Sola Dönülmez", "C) Sağa Dönülmez", "D) İleri Mecburi"), 0))
                soruListesi.add(Soru(R.drawable.levha54, listOf("A) U Dönüşü Yasak", "B) Girişi Olmayan Yol", "C) Dur", "D) Yol Ver"), 1))
                soruListesi.add(Soru(R.drawable.levha55, listOf("A) Girişi Olmayan Yol", "B) Dikkat", "C) Taşıt Trafiğine Kapalı Yol", "D) Yol ver"), 2))
                soruListesi.add(Soru(R.drawable.levha56, listOf("A) Motosiklet Hariç Taşıt Trafiğine Kapalı Yol", "B) Treyler Giremez", "C) Motosiklet Giremez", "D) Mopet Giremez"), 0))
                soruListesi.add(Soru(R.drawable.levha57, listOf("A) Kamyon Giremez", "B) Mopet Giremez", "C) Motosiklet Giremez", "D) Bisiklet Giremez"), 2))
                soruListesi.add(Soru(R.drawable.levha58, listOf("A) Motosiklet Hariç Taşıt Trafiğine Kapalı Yol", "B) Motosiklet Giremez", "C) Mopet Giremez", "D) Bisiklet Giremez"), 3))
                soruListesi.add(Soru(R.drawable.levha59, listOf("A) Bisiklet Giremez", "B) Mopet Giremez", "C) Motosiklet Giremez", "D) Treyler Giremez"), 1))
                soruListesi.add(Soru(R.drawable.levha60, listOf("A) Kamyon Giremez", "B) Mopet Giremez", "C) Motosiklet Giremez", "D) Bisiklet Giremez"), 0))
                soruListesi.add(Soru(R.drawable.levha61, listOf("A) Kamyon Giremez", "B) Otobüs Giremez", "C) Bisiklet Giremez", "D) Motosiklet Giremez"), 1))
                soruListesi.add(Soru(R.drawable.levha62, listOf("A) Otobüs Giremez", "B) Kamyon Giremez", "C) Treyler Giremez", "D) Motosiklet Giremez"), 2))
                soruListesi.add(Soru(R.drawable.levha63, listOf("A) Yol Ver", "B) Yaya Giremez", "C) Treyler Giremez", "D) Motosiklet Giremez"), 1))
                soruListesi.add(Soru(R.drawable.levha64, listOf("A) At Arabası Giremez", "B) Motosiklet Giremez", "C) Bisiklet Giremez", "D) Kamyon Giremez"), 0))
                soruListesi.add(Soru(R.drawable.levha65, listOf("A) Tarım Traktörü Giremez", "B) Otobüs Giremez", "C) El Arabası Giremez", "D) Mopet Giremez"), 2))
                soruListesi.add(Soru(R.drawable.levha66, listOf("A) At Arabası Giremez", "B) Kamyon Giremez", "C) El Arabası Giremez", "D) Tarım Traktörü Giremez"), 3))
                soruListesi.add(Soru(R.drawable.levha67, listOf("A) Belirli Miktardan Fazla Parlayıcı ve Patlayıcı Madde Taşıyan Taşıt Giremez", "B) Otobüs Giremez", "C) Tehlikeli Madde Taşıyan Taşıt Giremez", "D) Motosiklet Giremez"), 0))
                soruListesi.add(Soru(R.drawable.levha68, listOf("A) Belirli Miktardan Fazla Parlayıcı ve Patlayıcı Madde Taşıyan Taşıt Giremez", "B) Kamyon Giremez", "C) Tehlikeli Madde Taşıyan Taşıt Giremez", "D) Mopet Giremez"), 2))
                soruListesi.add(Soru(R.drawable.levha69, listOf("A) Tehlikeli Madde Taşıyan Taşıt Giremez", "B) Belirli Miktarlardan Fazla Su Kirletici Madde Taşıyan Taşıt Giremez", "C) Motorlu Taşıt Giremez", "D) Motosiklet Giremez"), 1))
                soruListesi.add(Soru(R.drawable.levha70, listOf("A) Motorlu Taşıt Giremez", "B) Mopet Giremez", "C) Tehlikeli Madde Taşıyan Taşıt Giremez", "D) Taşıt Giremez"), 0))
                soruListesi.add(Soru(R.drawable.levha71, listOf("A) Motorlu Taşıt Giremez", "B) Bisiklet Giremez", "C) Tehlikeli Madde Taşıyan Taşıt Giremez", "D) Taşıt Giremez"), 3))
                soruListesi.add(Soru(R.drawable.levha72, listOf("A) Uzunluğu ... Metreden Fazla Olan Taşıtlar Giremez", "B) Yüksekliği ... Metreden Fazla Olan Taşıtlar Giremez", "C) Genişliği ... Metreden Fazla Olan Taşıtlar Giremez", "D) Öndeki Taşıt ... Metreden Daha Yakın Takip Edilemez"), 2))
                soruListesi.add(Soru(R.drawable.levha73, listOf("A) Genişliği ... Metreden Fazla Olan Taşıtlar Giremez", "B) Yüksekliği ... Metreden Fazla Olan Taşıtlar Giremez", "C) Uzunluğu ... Metreden Fazla Olan Taşıtlar Giremez", "D) Öndeki Taşıt ... Metreden Daha Yakın Takip Edilemez"), 1))
                soruListesi.add(Soru(R.drawable.levha74, listOf("A) Uzunluğu ... Metreden Fazla Olan Taşıtlar Giremez", "B) Yüksekliği ... Metreden Fazla Olan Taşıtlar Giremez", "C) Genişliği ... Metreden Fazla Olan Taşıtlar Giremez", "D) Öndeki Taşıt ... Metreden Daha Yakın Takip Edilemez"), 0))
                soruListesi.add(Soru(R.drawable.levha75, listOf("A) Yüksekliği ... Metreden Fazla Olan Taşıtlar Giremez", "B) Dingil Başına ... Tondan Fazla Yük Düşen Taşıtlar Giremez", "C) Genişliği ... Metreden Fazla Olan Taşıtlar Giremez", "D) Öndeki Taşıt ... Metreden Daha Yakın Takip Edilemez"), 1))
                soruListesi.add(Soru(R.drawable.levha76, listOf("A) Uzunluğu ... Metreden Fazla Olan Taşıtlar Giremez", "B) Yüksekliği ... Metreden Fazla Olan Taşıtlar Giremez", "C) Dingil Başına ... Tondan Fazla Yük Düşen Taşıtlar Giremez", "D) Yük Ağırlığı ... Tondan Fazla Olan Taşıtlar Giremez"),  3))
                soruListesi.add(Soru(R.drawable.levha77, listOf("A) Genişliği ... Metreden Fazla Olan Taşıtlar Giremez", "B) Yüksekliği ... Metreden Fazla Olan Taşıtlar Giremez", "C) Öndeki Taşıt ... Metreden Daha Yakın Takip Edilemez", "D) Dingil Başına ... Tondan Fazla Yük Düşen Taşıtlar Giremez"), 2))
                soruListesi.add(Soru(R.drawable.levha78, listOf("A) Sağa Dönülmez", "B) Sola Dönülmez", "C) U Dönüşü Yapılmaz", "D) Karşıdan Gelene Yol Ver"), 0))
                soruListesi.add(Soru(R.drawable.levha79, listOf("A) Sağa Dönülmez", "B) Sola Dönülmez", "C) U Dönüşü Yapılmaz", "D) Taşıt Trafiğine Kapalı yol"), 1))
                soruListesi.add(Soru(R.drawable.levha80, listOf("A) Sola Dönülmez", "B) Sağa Dönülmez", "C) U Dönüşü Yapılmaz", "D) Girişi Olmayan Yol"), 2))
                soruListesi.add(Soru(R.drawable.levha81, listOf("A) Girişi Olmayan Yol", "B) Öndeki Taşıtı Geçmek Yasaktır", "C) Sola Dönülmez", "D) Sağa Dönülmez"), 1))
                soruListesi.add(Soru(R.drawable.levha82, listOf("A) Kamyonlar İçin Öndeki Taşıtı Geçmek Yasaktır", "B) Sola Dönülmez", "C) Öndeki Taşıtı Geçmek Yasaktır", "D) U Dönüşü Yapılmaz"), 0))
                soruListesi.add(Soru(R.drawable.levha83, listOf("A) Hız Sınırlama Sonu", "B) Mecburi Asgari Hız", "C) Girişi Olmayan Yol", "D) Azami Hız Sınırlaması"), 3))
                soruListesi.add(Soru(R.drawable.levha84, listOf("A) Okul Bölgesi Azami Hız Sınırlaması", "B) Azami Hız Sınırlaması", "C) Mecburi Asgari Hız", "D) Taşıt Giremez"), 0))
                soruListesi.add(Soru(R.drawable.levha85, listOf("A) Sağa Dönülmez", "B) Azami Hız Sınırlaması", "C) Sesli İkaz Cihazlarının Kullanımı Yasaktır", "D) Mopet Giremez"), 2))
                soruListesi.add(Soru(R.drawable.levha86, listOf("A) Sesli İkaz Cihazlarının Kullanımı Yasaktır", "B) Gümrük Durmadan Geçmek Yasaktır", "C) Taşıt Giremez", "D) Tehlikeli Madde Taşıyan Taşıt Giremez"), 1))
                soruListesi.add(Soru(R.drawable.levha87, listOf("A) Dur", "B) Yolda Çalışma", "C) Girişi Olmayan Yol", "D) Bütün Yasaklama ve Kısıtlamaların Sonu"), 3))
                soruListesi.add(Soru(R.drawable.levha88, listOf("A) Hız Sınırlama Sonu", "B) Mecburi Asgari Hız", "C) Azami Hız Sınırlaması", "D) Mecburi Asgari Hız Sonu"), 0))
                soruListesi.add(Soru(R.drawable.levha89, listOf("A) Motorlu Taşıt Giremez", "B) Geçme Yasağı Sonu", "C) Hız Sınırlama Sonu", "D) Kamyxonlar İçin Geçme Yasağı Sonu"), 1))
                soruListesi.add(Soru(R.drawable.levha90, listOf("A) Kamyon Giremez", "B) Geçme Yasağı Sonu", "C) Kamyonlar İçin Geçme Yasağı Sonu", "D) Motorlu Taşıt Giremez"), 2))
                soruListesi.add(Soru(R.drawable.levha91, listOf("A) Sola Mecburi Yön", "B) Soldan Gidiniz", "C) Sağdan Gidiniz", "D) Sağa Mecburi Yön"), 3))
                soruListesi.add(Soru(R.drawable.levha92, listOf("A) Sola Mecburi Yön", "B) Soldan Gidiniz", "C) Sağdan Gidiniz", "D) Sağa Mecburi Yön"), 0))
                soruListesi.add(Soru(R.drawable.levha93, listOf("A) Sağa Mecburi Yön", "B) İleri Mecburi Yön", "C) Her İki Yandan Gidiniz", "D) Sola Mecburi Yön"), 1))
                soruListesi.add(Soru(R.drawable.levha94, listOf("A) Sağdan Gidiniz", "B) İleri Mecburi Yön", "C) İleri ve Sağa Mecburi Yön", "D) Sağa Mecburi Yön"), 2))
                soruListesi.add(Soru(R.drawable.levha95, listOf("A) Soldan Gidiniz", "B) İleri Mecburi Yön", "C) Sola Mecburi Yön", "D) İleri ve Sola Mecburi Yön"), 3))
                soruListesi.add(Soru(R.drawable.levha96, listOf("A) Sağa ve Sola Mecburi Yön", "B) İlerde Sağa Mecburi Yön", "C) Her İki Yandan Gidiniz", "D) İlerde Sola Mecburi Yön"), 0))
                soruListesi.add(Soru(R.drawable.levha97, listOf("A) İlerde Sola Mecburi Yön", "B) Sağdan Gidiniz", "C) İlerde Sağa Mecburi Yön", "D) Soldan Gidiniz"), 2))
                soruListesi.add(Soru(R.drawable.levha98, listOf("A) İlerde Sola Mecburi Yön", "B) Soldan Gidiniz", "C) İlerde Sağa Mecburi Yön", "D) Sağdan Gidiniz"), 0))
                soruListesi.add(Soru(R.drawable.levha99, listOf("A) İlerde Sola Mecburi Yön", "B) Soldan Gidiniz", "C) İlerde Sağa Mecburi Yön", "D) İlerde Sağa Mecburi Yön"), 3))
                soruListesi.add(Soru(R.drawable.levha100, listOf("A) İlerde Sola Mecburi Yön", "B) Soldan Gidiniz", "C) Sağdan Gidiniz", "D) Sağdan Gidiniz"), 1))
                soruListesi.add(Soru(R.drawable.levha101, listOf("A) Sağa ve Sola Mecburi Yön", "B) İlerde Sağa Mecburi Yön", "C) Her İki Yandan Gidiniz", "D) İlerde Sola Mecburi Yön"), 2))
                soruListesi.add(Soru(R.drawable.levha102, listOf("A) Ada Etrafından Dönünüz", "B) İlerde Sağa Mecburi Yön", "C) Sağdan Gidiniz", "D) İlerde Sola Mecburi Yön"), 0))
                soruListesi.add(Soru(R.drawable.levha103, listOf("A) Motosiklet Giremez", "B) Mecburi Bisiklet Yolu", "C) Bisiklet Giremez", "D) Mecburi Bisiklet Yolu Sonu"), 1))
                soruListesi.add(Soru(R.drawable.levha104, listOf("A) Bisiklet Giremez", "B) Mecburi Bisiklet Yolu", "C) Motosiklet Giremez", "D) Mecburi Bisiklet Yolu Sonu"), 3))
                soruListesi.add(Soru(R.drawable.levha105, listOf("A) Mecburi Yaya Yolu Sonu", "B) Okul Geçidi", "C) Mecburi Yaya Yolu", "D) Yaya Giremez"), 2))
                soruListesi.add(Soru(R.drawable.levha106, listOf("A) Mecburi Yaya Yolu", "B) Yaya Giremez", "C) Okul Geçidi", "D) Mecburi Yaya Yolu Sonu"), 3))
                soruListesi.add(Soru(R.drawable.levha107, listOf("A) Mecburi Atlı Yolu", "B) At Arabası Giremez", "C) Mecburi Atlı Yolu Sonu", "D) Tarım Traktörü Giremez"), 0))
                soruListesi.add(Soru(R.drawable.levha108, listOf("A) Mecburi Atlı Yolu Sonu", "B) Tarım Traktörü Giremez", "C) Mecburi Atlı Yolu", "D) At Arabası Giremez"), 0))
                soruListesi.add(Soru(R.drawable.levha109, listOf("A) Hız Sınırlama Sonu", "B) Mecburi Asgari Hız", "C) Azami Hız Sınırlaması", "D) Mecburi Asgari Hız Sonu"), 1))
                soruListesi.add(Soru(R.drawable.levha110, listOf("A) Azami Hız Sınırlaması", "B) Mecburi Asgari Hız", "C) Hız Sınırlama Sonu", "D) Mecburi Asgari Hız Sonu"), 3))
                soruListesi.add(Soru(R.drawable.levha111, listOf("A) Taşıt Giremez", "B) Zincir Takmak Mecburiyeti Sonu", "C) Zincir Takmak Mecburidir", "D) Motorlu Taşıt Giremez"), 2))
                soruListesi.add(Soru(R.drawable.levha112, listOf("A) Motorlu Taşıt Giremez", "B) Zincir Takmak Mecburiyeti Sonu", "C) Zincir Takmak Mecburidir", "D) Taşıt Giremez"), 1))
                soruListesi.add(Soru(R.drawable.levha113, listOf("A) Tehlikeli Madde Taşıyan Taşıtların İzleyecekleri Mecburi Yön", "B) Soldan Gidiniz", "C) Tehlikeli Madde Taşıyan Taşıt Giremez", "D) Belirli Miktardan Fazla Parlayıcı ve Patlayıcı Madde Taşıyan Taşıt Giremez"), 0))
                soruListesi.add(Soru(R.drawable.levha114, listOf("A) Tehlikeli Madde Taşıyan Taşıt Giremez", "B) Yolda Çalışma", "C) Tehlikeli Madde Taşıyan Taşıtların İzleyecekleri Mecburi Yön", "D) İleri Mecburi yön"), 2))
                soruListesi.add(Soru(R.drawable.levha115, listOf("A) Sağdan Gidiniz", "B) Tehlikeli Madde Taşıyan Taşıt Giremez", "C) Sağa Mecburi Yön", "D) Tehlikeli Madde Taşıyan Taşıtların İzleyecekleri Mecburi Yön"), 3))
                soruListesi.add(Soru(R.drawable.levha116, listOf("A) Yaya Giremez", "B) Yayalar Ve Bisikletliler Tarafından Kullanılabilen Yol", "C) Yayalar Ve Bisikletliler Tarafından Kullanılabilen Yolun Sonu", "D) Taşıt Giremez"), 1))
                soruListesi.add(Soru(R.drawable.levha117, listOf("A) Yayalar Ve Bisikletliler Tarafından Kullanılabilen Yolun Sonu", "B) Taşıt Giremez", "C) Yayalar Ve Bisikletliler Tarafından Kullanılabilen Yol", "D) Yaya Giremez"), 0))
                soruListesi.add(Soru(R.drawable.levha118, listOf("A) Yayalar Ve Bisikletliler Tarafından Kullanılabilen Yol", "B) Yolda Çalışma", "C) Yaya Giremez", "D) Yayalar Ve Bisikletliler İçin Ayrı Ayrı Kullanılabilen Yol"), 3))
                soruListesi.add(Soru(R.drawable.levha119, listOf("A) Yaya Giremez", "B) Yayalar Ve Bisikletliler Tarafından Kullanılabilen Yolun Sonu", "C) Yayalar Ve Bisikletliler İçin Ayrı Ayrı Kullanılabilen Yolun Sonu", "D) Yayalar Ve Bisikletliler Tarafından Kullanılabilen Yol"), 1))
            }
            "kategori_3" -> {
                soruListesi.add(Soru(R.drawable.levha146, listOf("A) Kavşak Öncesi Yol Levhası", "B) Coğrafi Bilgi Levhası", "C) Meskun Mahal Levhası", "D) Kaplama Üstü Yön Levhası"), 0))
                soruListesi.add(Soru(R.drawable.levha147, listOf("A) İleri Çıkmaz Yol", "B) Tek Yönlü Yol", "C) Girişi Olmayan Yol Levhası", "D) Önceliği Olan Yön"), 2))
                soruListesi.add(Soru(R.drawable.levha148, listOf("A) U Dönüşü Levhası", "B) İleriki Kavşakta Sola Dönüş Yasağı Gösteren Yön Tabelası", "C) Kavşak İçi Yön Levhası", "D) Kavşak Öncesi Şerit Seçimi Levhası"), 1))
                soruListesi.add(Soru(R.drawable.levha149, listOf("A) Kavşak İçi Yön Levhası", "B) Kavşak Öncesi Şerit Seçimi Levhası", "C) İleri Çıkmaz Yol", "D) U Dönüşü Levhası"), 1))
                soruListesi.add(Soru(R.drawable.levha150, listOf("A) Kaplama Üstü Yön Levhası", "B) Taşıt Giremez", "C) Otoyol Başlangıcı Sonu", "D) Otoyol Başlangıcı"), 3))
                soruListesi.add(Soru(R.drawable.levha151, listOf("A) Otoyol Başlangıcı Sonu", "B) Kavşak Öncesi Yol Levhası", "C) Otoyol Başlangıcı", "D) Motorlu Taşıt Yasağı Sonu"), 0))
                soruListesi.add(Soru(R.drawable.levha152, listOf("A) Tamirhane", "B) İlk Yardım", "C) Hastane", "D) Akaryakıt İstasyonu"), 2))
                soruListesi.add(Soru(R.drawable.levha153, listOf("A) İl Sınırı Levhası", "B) Türkiye Devlet Sınırı Levhası", "C) Meskun Mahal Levhası(İl Merkezi)", "D) Meskun Mahal Levhası(İlçe Merkezi)"), 1))
                soruListesi.add(Soru(R.drawable.levha154, listOf("A) Türkiye Hız Sınırları Levhası", "B) Türkiye Devlet Sınırı Levhası", "C) Azami Hız Sınırı Levhası", "D) Asgari Hız Sınırı Levhası"), 0))
                soruListesi.add(Soru(R.drawable.levha155, listOf("A) Çeşme", "B) Dinlenme Yeri", "C) Lokanta", "D) Durak"), 3))
                soruListesi.add(Soru(R.drawable.levha156, listOf("A) Hastane", "B) İlk Yardım", "C) Tamirhane", "D) Akaryakıt İstasyonu"), 1))
                soruListesi.add(Soru(R.drawable.levha157, listOf("A) Kaçış Rampası", "B) Anayol Sonu", "C) Anayol", "D) Girişi Olmayan Yol"), 2))
                soruListesi.add(Soru(R.drawable.levha158, listOf("A) Anayol Sonu", "B) Girişi Olmayan Yol", "C) Taşıt Trafiğine Kapalı Yol", "D) Anayol"), 0))
                soruListesi.add(Soru(R.drawable.levha159, listOf("A) Yaya Geçidi", "B) Trafik Cebi", "C) Park Yeri", "D) Taralı Alana Girilmez"), 1))
                soruListesi.add(Soru(R.drawable.levha160, listOf("A) Hastane", "B) İlk Yardım", "C) Tamirhane", "D) Turizm Danışma"), 3))
                soruListesi.add(Soru(R.drawable.levha161, listOf("A) Alt Geçit-Üst Geçit", "B) Üst Geçit-Alt Geçit", "C) Alt Geçit-Alt Geçit", "D) Üst Geçit-Üst Geçit"), 0))
                soruListesi.add(Soru(R.drawable.levha162, listOf("A) Rampalı Yaya Alt Geçidi", "B) Üst Geçit", "C) Rampalı Yaya Üst Geçidi", "D) Park Yeri"), 2))
                soruListesi.add(Soru(R.drawable.levha163, listOf("A) Yüzülmez", "B) Gençlik Kampı", "C) Çeşme", "D) Yüzme Yeri"), 3))
                soruListesi.add(Soru(R.drawable.levha164, listOf("A) Tramvay Durağı", "B) İstasyon", "C) Treyler Giremez", "D) Kamyon Giremez"), 1))
                soruListesi.add(Soru(R.drawable.levha165, listOf("A) Tramvay Durağı", "B) İlk Yardım", "C) Treyler Giremez", "D) İstasyon"), 0))
                soruListesi.add(Soru(R.drawable.levha166, listOf("A) Akaryakıt İstasyonu", "B) İlk Yardım", "C) Tamirhane", "D) Sanayi Bölgesi(OSB)"), 3))
                soruListesi.add(Soru(R.drawable.levha167, listOf("A) Hastane", "B) Telefon", "C) Elektronik Denetleme Sistemi", "D) Kamera"), 2))
                soruListesi.add(Soru(R.drawable.levha168, listOf("A) Çeşme", "B) Akaryakıt İstasyonu", "C) Kamp Yeri", "D) Telefon"), 1))
                soruListesi.add(Soru(R.drawable.levha169, listOf("A) Kamp Yeri", "B) Piknik Yeri", "C) Gençlik Kampı", "D) Karavanlı Kamp Yeri"), 1))
                soruListesi.add(Soru(R.drawable.levha170, listOf("A) Gençlik Kampı", "B) Kamp Yeri", "C) Karavanlı Kamp Yeri", "D) Piknik Yeri"), 0))
                soruListesi.add(Soru(R.drawable.levha171, listOf("A) Piknik Yeri", "B) Kamp Yeri", "C) Çadırlı ve Karavanlı Kamp Yeri", "D) Karavanlı Kamp Yeri"), 3))
                soruListesi.add(Soru(R.drawable.levha172, listOf("A) Karavanlı Kamp Yeri", "B) Otel Veya Motel", "C) Kamp Yeri", "D) Piknik Yeri"), 2))
                soruListesi.add(Soru(R.drawable.levha173, listOf("A) İl Sınırı Levhası", "B) Meskun Mahal Levhası(İl Merkezi)", "C) Meskun Mahal Levhası(İlçe Merkezi)", "D) Coğrafi Bilgi Levhası"), 1))
                soruListesi.add(Soru(R.drawable.levha174, listOf("A) Meskun Mahal Levhası(İl Merkezi)", "B) Kvşak Öncesi Yön Levhası", "C) Meskun Mahal Levhası(İlçe Merkezi)", "D) Meskun Mahal Levhası(Köy-Belde-Bucak Merkezi)"), 2))
                soruListesi.add(Soru(R.drawable.levha175, listOf("A) Mesafe Levhası", "B) İl Sınırı Levhası", "C) İl Sınırı Levhası", "D) Meskun Mahal Levhası(İl Merkezi)"), 0))
            }
            "kategori_4" -> {
                soruListesi.add(Soru(R.drawable.levha120, listOf("A) Duraklamak ve Park Etmek Yasaktır", "B) Park Etmek Yasaktır", "C) Duraklamak Yasak", "D) Giriş Yasak"), 1))
                soruListesi.add(Soru(R.drawable.levha121, listOf("A) Park Etmek Yasaktır", "B) Giriş Yasak", "C) Duraklamak Yasak", "D) Duraklamak ve Park Etmek Yasaktır"), 3))
                soruListesi.add(Soru(R.drawable.levha122, listOf("A) Park Yeri", "B) Park Etmek Yasaktır", "C) Duraklamak Yasak", "D) Giriş Yasak"), 0))
                soruListesi.add(Soru(R.drawable.levha123, listOf("A) Giriş Yasak", "B) Park Etmek Yasaktır", "C) Park Yeri", "D) Duraklamak ve Park Etmek Yasaktır"), 2))
                soruListesi.add(Soru(R.drawable.levha124, listOf("A) Park Yeri", "B) Kapalı Park Yeri", "C) Duraklamak Yasak", "D) Giriş Yasak"), 0))
                soruListesi.add(Soru(R.drawable.levha125, listOf("A) Park Yeri", "B) Giriş Yasak", "C) Duraklamak ve Park Etmek Yasaktır", "D) Kapalı Park Yeri"), 3))
                soruListesi.add(Soru(R.drawable.levha126, listOf("A) Park Etmek Yasaktır", "B) Park Yeri(Metrodan Yararlanacaklar İçin)", "C) Park Yeri", "D) Giriş Yasak"), 1))
                soruListesi.add(Soru(R.drawable.levha127, listOf("A) Duraklamak Yasak", "B) Giriş Yasak", "C) Park Yeri(Tramvaydan Yararlanacaklar İçin)", "D) Duraklamak ve Park Etmek Yasaktır"), 2))
            }
            "kategori_5" -> {
                soruListesi.add(Soru(R.drawable.levha128, listOf("A) Engelli Sürücüler İçin Park Yeri", "B) Yavaşla", "C) Park Yeri", "D) Yaya Giremez"), 0))
                soruListesi.add(Soru(R.drawable.levha129, listOf("A) Her İki Yandan Gidiniz ", "B) Sağdan Gidiniz", "C) Bu Şerit Sadece İleri Yönde Seyir İçindir", "D) İleri Mecburi Yön"), 2))
                soruListesi.add(Soru(R.drawable.levha130, listOf("A) Bu Şerit Sadece İleri Yönde Seyir İçindir", "B) Bu Şerit Sadece İleri Seyir veya Sola Dönüş İçindir", "C) Soldan Gidiniz", "D) İleri ve Sola Mecburi Yön"), 1))
                soruListesi.add(Soru(R.drawable.levha131, listOf("A) Sağdan Gidiniz", "B) Bu Şerit Sadece İleri Yönde Seyir İçindir", "C) İleri ve Sağa Mecburi Yön", "D) Bu Şerit Sadece İleri Seyir veya Sağa Dönüş İçindir"), 3))
                soruListesi.add(Soru(R.drawable.levha132, listOf("A) Yol Ver", "B) Park Etmek Yasaktır", "C) Taralı Alana Girilmez", "D) Girişi Olmayan Yol"), 0))
                soruListesi.add(Soru(R.drawable.levha133, listOf("A) Giriş Yasak", "B) Duraklamak ve Park Etmek Yasaktır", "C) Yaya Geçidi", "D) Taralı Alana Girilmez"), 3))
                soruListesi.add(Soru(R.drawable.levha134, listOf("A) Duraklamak Yasak", "B) Yaya Geçidi", "C) Taralı Alana Girilmez", "D) Giriş Yasak"), 1))
                soruListesi.add(Soru(R.drawable.levha135, listOf("A) Park Yeri", "B) Park Etmek Yasaktır", "C) Yaya Geçidi", "D) Duraklamak Yasak"), 2))
                soruListesi.add(Soru(R.drawable.levha136, listOf("A) Azami Hız", "B) Dur", "C) Duraklamak Yasak", "D) Giriş Yasak"), 0))
                soruListesi.add(Soru(R.drawable.levha137, listOf("A) Yaya Geçidi", "B) Taralı Alana Girilmez", "C) Duraklamak Yasak", "D) Dur"), 3))
                soruListesi.add(Soru(R.drawable.levha138, listOf("A) Giriş Yasak", "B) Bisiklet Yolu", "C) Duraklamak Yasak", "D) Bisiklet Giremez"), 1))
                soruListesi.add(Soru(R.drawable.levha139, listOf("A) Devamlı Çizgi:Öndeki Aracı Geçmek Yasaktır", "B) Kesikli Çizgi:Öndeki Araç Geçilebilir", "C) Duraklamak Yasak", "D) Taralı Alana Girilmez"), 1))
                soruListesi.add(Soru(R.drawable.levha140, listOf("A) Park Yeri", "B) Kesikli Çizgi:Öndeki Araç Geçilebilir", "C) Devamlı Çizgi:Öndeki Aracı Geçmek Yasaktır", "D) Tırmanma Şeridi:Orta Şerit Sadece Geçiş İçindir.Devamlı Olarak İşgal Edilmez"), 2))
                soruListesi.add(Soru(R.drawable.levha141, listOf("A) Tırmanma Şeridi:Orta Şerit Sadece Geçiş İçindir.Devamlı Olarak İşgal Edilmez", "B) Kesikli Çizgi:Öndeki Araç Geçilebilir", "C) Devamlı Çizgi:Öndeki Aracı Geçmek Yasaktır", "D) Taralı Alana Girilmez"), 0))
                soruListesi.add(Soru(R.drawable.levha142, listOf("A) Kesikli Çizgi:Öndeki Araç Geçilebilir", "B) Devamlı Çizgi:Öndeki Aracı Geçmek Yasaktır", "C) Tırmanma Şeridi:Orta Şerit Sadece Geçiş İçindir.Devamlı Olarak İşgal Edilmez", "D) Kesikli Ve Devamlı Çizgi:Kesikli Çizgi Tarafındaki Araç Öndeki Aracı Geçecektir.Devamlı Çizgi Tarafındaki Aracın Öndeki Aracı Geçmesi Yasaktır"), 3))
                soruListesi.add(Soru(R.drawable.levha143, listOf("A) Devamlı Çizgi:Öndeki Aracı Geçmek Yasaktır", "B) Kesikli Çizgi:Öndeki Araç Geçilebilir", "C) İki Devamlı Çizgi:Her İki Yöndeki Araçlar Da Çizginin Diğer Tarafına Geçemez", "D) Tırmanma Şeridi:Orta Şerit Sadece Geçiş İçindir.Devamlı Olarak İşgal Edilmez"), 2))
                soruListesi.add(Soru(R.drawable.levha144, listOf("A) Ayrılma-Katılma-Bölünmüş Yol Başlangıcı", "B) Katılma-Ayrılma-Bölünmüş Yol Başlangıcı", "C) Bölünmüş Yol Başlangıcı-Ayrılma-Katılma", "D) Ayrılma-Bölünmüş Yol Başlangıcı-Katılma"), 0))
            }
            "kategori_6" ->{
                soruListesi.add(Soru(R.drawable.levha56, listOf("A) Motosiklet Hariç Taşıt Trafiğine Kapalı Yol", "B) Treyler Giremez", "C) Motosiklet Giremez", "D) Mopet Giremez"), 0))
                soruListesi.add(Soru(R.drawable.levha13, listOf("A) Kaygan Yol", "B) İki Yönlü Trafik", "C) Kasisli Yol", "D) Yaya Geçidi"), 0))
                soruListesi.add(Soru(R.drawable.levha128, listOf("A) Engelli Sürücüler İçin Park Yeri", "B) Yavaşla", "C) Park Yeri", "D) Yaya Giremez"), 0))
                soruListesi.add(Soru(R.drawable.levha93, listOf("A) Sağa Mecburi Yön", "B) İleri Mecburi Yön", "C) Her İki Yandan Gidiniz", "D) Sola Mecburi Yön"), 1))
                soruListesi.add(Soru(R.drawable.levha1, listOf("A) Sola Tehlikeli Viraj", "B) Sağa Tehlikeli Viraj", "C) Kasisli Yol", "D) Yol Ver"), 1))
                soruListesi.add(Soru(R.drawable.levha46, listOf("A) Dönüş Adası Ek Levhası", "B) İki Yönlü Trafik", "C) Yolda Çalışma", "D) Düşük Banket"), 0))
                soruListesi.add(Soru(R.drawable.levha2, listOf("A) Sola Tehlikeli Devamlı Virajlar", "B) Gevşek Şev", "C) Soldan Anayol Girişi", "D) Sola Tehlikeli Viraj"), 3))
                soruListesi.add(Soru(R.drawable.levha122, listOf("A) Park Yeri", "B) Park Etmek Yasaktır", "C) Duraklamak Yasak", "D) Giriş Yasak"), 0))
                soruListesi.add(Soru(R.drawable.levha3, listOf("A) Sağa Tehlikeli Devamlı Virajlar", "B) Sola Tehlikeli Devamlı Virajlar", "C) İki Taraftan Daralan Kaplama", "D) Sağa Tehlikeli Viraj"), 0))
                soruListesi.add(Soru(R.drawable.levha131, listOf("A) Sağdan Gidiniz", "B) Bu Şerit Sadece İleri Yönde Seyir İçindir", "C) İleri ve Sağa Mecburi Yön", "D) Bu Şerit Sadece İleri Seyir veya Sağa Dönüş İçindir"), 3))
                soruListesi.add(Soru(R.drawable.levha4, listOf("A) Soldan Anayol Girişi", "B) Kontrolsüz Kavşak", "C) Sola Tehlikeli Devamlı Virajlar", "D) Dikkat"), 2))
                soruListesi.add(Soru(R.drawable.levha94, listOf("A) Sağdan Gidiniz", "B) İleri Mecburi Yön", "C) İleri ve Sağa Mecburi Yön", "D) Sağa Mecburi Yön"), 2))
                soruListesi.add(Soru(R.drawable.levha15, listOf("A) Yol Ver", "B) Gevşek Şev", "C) Deniz-Nehir Kıyısında Biten Yol", "D) Dikkat"), 1))
                soruListesi.add(Soru(R.drawable.levha136, listOf("A) Azami Hız", "B) Dur", "C) Duraklamak Yasak", "D) Giriş Yasak"), 0))
                soruListesi.add(Soru(R.drawable.levha148, listOf("A) U Dönüşü Levhası", "B) İleriki Kavşakta Sola Dönüş Yasağı Gösteren Yön Tabelası", "C) Kavşak İçi Yön Levhası", "D) Kavşak Öncesi Şerit Seçimi Levhası"), 1))
                soruListesi.add(Soru(R.drawable.levha11, listOf("A) Açılan Köprü", "B) İki Yönlü Trafik", "C) Dikkat", "D) Deniz-Nehir Kıyısında Biten Yol"), 3))
                soruListesi.add(Soru(R.drawable.levha14, listOf("A) Dikkat", "B) Gevşek Şev", "C) Gevşek Malzemeli Zemin", "D) Kasisli Yol"), 2))
                soruListesi.add(Soru(R.drawable.levha92, listOf("A) Sola Mecburi Yön", "B) Soldan Gidiniz", "C) Sağdan Gidiniz", "D) Sağa Mecburi Yön"), 0))
                soruListesi.add(Soru(R.drawable.levha150, listOf("A) Kaplama Üstü Yön Levhası", "B) Taşıt Giremez", "C) Otoyol Başlangıcı Sonu", "D) Otoyol Başlangıcı"), 3))
                soruListesi.add(Soru(R.drawable.levha108, listOf("A) Mecburi Atlı Yolu Sonu", "B) Tarım Traktörü Giremez", "C) Mecburi Atlı Yolu", "D) At Arabası Giremez"), 0))
                soruListesi.add(Soru(R.drawable.levha51, listOf("A) Girişi Olmayan Yol", "B) Dur", "C) Park Yapılmaz", "D) Yol Ver"), 3))
                soruListesi.add(Soru(R.drawable.levha109, listOf("A) Hız Sınırlama Sonu", "B) Mecburi Asgari Hız", "C) Azami Hız Sınırlaması", "D) Mecburi Asgari Hız Sonu"), 1))
                soruListesi.add(Soru(R.drawable.levha71, listOf("A) Motorlu Taşıt Giremez", "B) Bisiklet Giremez", "C) Tehlikeli Madde Taşıyan Taşıt Giremez", "D) Taşıt Giremez"), 3))
                soruListesi.add(Soru(R.drawable.levha72, listOf("A) Uzunluğu ... Metreden Fazla Olan Taşıtlar Giremez", "B) Yüksekliği ... Metreden Fazla Olan Taşıtlar Giremez", "C) Genişliği ... Metreden Fazla Olan Taşıtlar Giremez", "D) Öndeki Taşıt ... Metreden Daha Yakın Takip Edilemez"), 2))
                soruListesi.add(Soru(R.drawable.levha36, listOf("A) Kontrolsüz Demiryolu Geçidi", "B) İki Yönlü Trafik", "C) Deniz-Nehir Kıyısında Biten Yol", "D) Kontrollü Demiryolu Geçidi"), 3))
                soruListesi.add(Soru(R.drawable.levha87, listOf("A) Dur", "B) Yolda Çalışma", "C) Girişi Olmayan Yol", "D) Bütün Yasaklama ve Kısıtlamaların Sonu"), 3))
                soruListesi.add(Soru(R.drawable.levha120, listOf("A) Duraklamak ve Park Etmek Yasaktır", "B) Park Etmek Yasaktır", "C) Duraklamak Yasak", "D) Giriş Yasak"), 1))
                soruListesi.add(Soru(R.drawable.levha10, listOf("A) Açılan Köprü", "B) İki Yönlü Trafik", "C) Deniz-Nehir Kıyısında Biten Yol", "D) Kasisli Yol"), 0))
                soruListesi.add(Soru(R.drawable.levha166, listOf("A) Akaryakıt İstasyonu", "B) İlk Yardım", "C) Tamirhane", "D) Sanayi Bölgesi(OSB)"), 3))
                soruListesi.add(Soru(R.drawable.levha145, listOf("A) Köprü Başı Levhası(Sağ-Sol)", "B) Yolda Çalışma", "C) Deniz-Nehir Kıyısında Biten Yol", "D) Demiryolu Hemzemin Geçit Yaklaşımı(Sağ-Sol)"), 0))
                soruListesi.add(Soru(R.drawable.levha27, listOf("A) AnaYol Tali Yol Kavşağı", "B) İki Yönlü Trafik", "C) Kontrolsüz Kavşak", "D) Kasisli Yol"), 2))
                soruListesi.add(Soru(R.drawable.levha28, listOf("A) AnaYol Tali Yol Kavşağı", "B) İki Yönlü Trafik", "C) Yolda Çalışma", "D) Kasisli Yol"), 0))
                soruListesi.add(Soru(R.drawable.levha167, listOf("A) Hastane", "B) Telefon", "C) Elektronik Denetleme Sistemi", "D) Kamera"), 2))
                soruListesi.add(Soru(R.drawable.levha168, listOf("A) Çeşme", "B) Akaryakıt İstasyonu", "C) Kamp Yeri", "D) Telefon"), 1))
                soruListesi.add(Soru(R.drawable.levha12, listOf("A) Gevşek Şev", "B) Kasisli Yol", "C) Deniz-Nehir Kıyısında Biten Yol", "D) Kaygan Yol"), 1))
                soruListesi.add(Soru(R.drawable.levha53, listOf("A) Karşıdan Gelene Yol Ver", "B) Sola Dönülmez", "C) Sağa Dönülmez", "D) İleri Mecburi"), 0))
                soruListesi.add(Soru(R.drawable.levha43, listOf("A) Düşük Banket", "B) Tehlikeli Viraj Yön Levhası", "C) Dönüş Adası Ek Levhası", "D) Gizli Buzlanma"), 1))
                soruListesi.add(Soru(R.drawable.levha44, listOf("A) Tehlikeli Viraj Yön Levhası", "B) İki Yönlü Trafik", "C) Yolda Çalışma", "D) Dönel Kavşak"), 0))
                soruListesi.add(Soru(R.drawable.levha45, listOf("A) Dönüş Adası Ek Levhası", "B) Düşük Banket", "C) Refüj Başı Ek Levhaları", "D) Trafik Sıkışıklığı"), 2))
                soruListesi.add(Soru(R.drawable.levha16, listOf("A) Bisiklet Geçebilir", "B) Yol Ver", "C) Okul Geçidi", "D) Yaya Geçidi"), 3))
                soruListesi.add(Soru(R.drawable.levha17, listOf("A) Okul Geçidi", "B) Yaya Geçidi", "C) Yolda Çalışma", "D) Kasisli Yol"), 0))
                soruListesi.add(Soru(R.drawable.levha169, listOf("A) Kamp Yeri", "B) Piknik Yeri", "C) Gençlik Kampı", "D) Karavanlı Kamp Yeri"), 1))
                soruListesi.add(Soru(R.drawable.levha140, listOf("A) Park Yeri", "B) Kesikli Çizgi:Öndeki Araç Geçilebilir", "C) Devamlı Çizgi:Öndeki Aracı Geçmek Yasaktır", "D) Tırmanma Şeridi:Orta Şerit Sadece Geçiş İçindir.Devamlı Olarak İşgal Edilmez"), 2))
                soruListesi.add(Soru(R.drawable.levha121, listOf("A) Park Etmek Yasaktır", "B) Giriş Yasak", "C) Duraklamak Yasak", "D) Duraklamak ve Park Etmek Yasaktır"), 3))
                soruListesi.add(Soru(R.drawable.levha170, listOf("A) Gençlik Kampı", "B) Kamp Yeri", "C) Karavanlı Kamp Yeri", "D) Piknik Yeri"), 0))
                soruListesi.add(Soru(R.drawable.levha67, listOf("A) Belirli Miktardan Fazla Parlayıcı ve Patlayıcı Madde Taşıyan Taşıt Giremez", "B) Otobüs Giremez", "C) Tehlikeli Madde Taşıyan Taşıt Giremez", "D) Motosiklet Giremez"), 0))
                soruListesi.add(Soru(R.drawable.levha55, listOf("A) Girişi Olmayan Yol", "B) Dikkat", "C) Taşıt Trafiğine Kapalı Yol", "D) Yol ver"), 2))
                soruListesi.add(Soru(R.drawable.levha110, listOf("A) Azami Hız Sınırlaması", "B) Mecburi Asgari Hız", "C) Hız Sınırlama Sonu", "D) Mecburi Asgari Hız Sonu"), 3))
                soruListesi.add(Soru(R.drawable.levha83, listOf("A) Hız Sınırlama Sonu", "B) Mecburi Asgari Hız", "C) Girişi Olmayan Yol", "D) Azami Hız Sınırlaması"), 3))
                soruListesi.add(Soru(R.drawable.levha84, listOf("A) Okul Bölgesi Azami Hız Sınırlaması", "B) Azami Hız Sınırlaması", "C) Mecburi Asgari Hız", "D) Taşıt Giremez"), 0))

            }
            else -> {
                soruListesi.add(Soru(R.drawable.levha1, listOf("Sola Tehlikeli Viraj", "Sağa Tehlikeli Viraj", "Kasisli Yol", "Yol Ver"), 1))
            }
        }
    }

    private fun soruyuEkranaBas() {
        if (simdikiSoruIndex >= soruListesi.size) {
            sonucEkraniGoster()
            return
        }

        if (simdikiSoruIndex == 0) {
            btnGeri.visibility = View.INVISIBLE
        } else {
            btnGeri.visibility = View.VISIBLE
        }
        if (simdikiSoruIndex == soruListesi.size - 1) {
            btnIleri.visibility = View.INVISIBLE
        } else {
            btnIleri.visibility = View.VISIBLE
        }

        txtSoruSayisi.text = "Soru: ${simdikiSoruIndex + 1} / ${soruListesi.size}"

        val suankiSoru = soruListesi[simdikiSoruIndex]
        imgLevha.setImageResource(suankiSoru.resimId)

        val soruCevaplandi = cevaplananSorular.contains(simdikiSoruIndex)

        for (i in 0 until butonlar.size) {
            butonlar[i].text = suankiSoru.secenekler[i]
            butonlar[i].isEnabled = !soruCevaplandi
            butonlar[i].setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray))

            if (soruCevaplandi && i == suankiSoru.dogruCevapIndex) {
                butonlar[i].setBackgroundColor(Color.GREEN)
            }
        }
    }

    private fun cevabiKontrolEt(secilenIndex: Int) {
        if (cevaplananSorular.contains(simdikiSoruIndex)) return

        val suankiSoru = soruListesi[simdikiSoruIndex]
        val dogruCevapIndex = suankiSoru.dogruCevapIndex

        cevaplananSorular.add(simdikiSoruIndex)

        butonlar.forEach { it.isEnabled = false }

        if (secilenIndex == dogruCevapIndex) {
            butonlar[secilenIndex].setBackgroundColor(Color.GREEN)
            dogruSayisi++
        } else {
            butonlar[secilenIndex].setBackgroundColor(Color.RED)
            butonlar[dogruCevapIndex].setBackgroundColor(Color.GREEN)
            yanlisSayisi++
        }

        Handler(Looper.getMainLooper()).postDelayed({
            if (simdikiSoruIndex < soruListesi.size - 1) {
                simdikiSoruIndex++
                soruyuEkranaBas()
            } else {
                sonucEkraniGoster()
            }
        }, 1500)
    }

    private fun sonucEkraniGoster() {
        geriSayimSayaci?.cancel()

        val intent = Intent(this, SonucActivity::class.java)
        intent.putExtra("DOGRU_SAYISI", dogruSayisi)
        intent.putExtra("YANLIS_SAYISI", yanlisSayisi)
        intent.putExtra("SECILEN_KATEGORI", secilenKategori)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        geriSayimSayaci?.cancel()
    }
}