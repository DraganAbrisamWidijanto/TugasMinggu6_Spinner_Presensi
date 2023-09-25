package com.example.tugasminggu6presensi

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.TextView
import android.widget.Toast
import com.example.tugasminggu6presensi.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Daftar opsi untuk spinner
        val presensiOptions = listOf("Hadir Tepat Waktu", "Sakit", "Terlambat", "Izin")

        // Adapter untuk spinner
        val spinnerAdapter = ArrayAdapter(this@MainActivity, R.layout.data_spinner, presensiOptions)
        val calendar = Calendar.getInstance()

        // Format tanggal yang dipilih
        var selectedDate = SimpleDateFormat("MMMM dd, yyyy",Locale.getDefault()).format(calendar.time)

        // Waktu yang dipilih
        var selectedTime = decideTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE))

        // Konfigurasi antarmuka pengguna
        with(binding) {
            // Mengatur adapter untuk spinner
            spinner.adapter = spinnerAdapter

            // Mendengarkan pemilihan pada spinner
            spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedPresensi = spinner.selectedItem.toString()
                    // Menampilkan atau menyembunyikan EditText berdasarkan pilihan pada spinner
                    if(selectedPresensi == presensiOptions[1] || selectedPresensi == presensiOptions[2] || selectedPresensi == presensiOptions[3]) {
                        youcantseeme.visibility = View.VISIBLE
                    } else {
                        youcantseeme.visibility = View.GONE
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Tindakan yang diambil ketika tidak ada yang dipilih
                    // Tidak ada tindakan yang diambil dalam contoh ini
                }
            }

            // Mendengarkan perubahan tanggal pada DatePicker
            datePicker.setOnDateChangeListener { _, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                selectedDate = dateFormat.format(calendar.time)
            }

            // Mendengarkan perubahan waktu pada TimePicker
            timePicker.setOnTimeChangedListener { _, hourOfDay, minute ->
                selectedTime = decideTime(hourOfDay, minute)
            }

            // Menangani klik pada tombol Submit
            submit.setOnClickListener {
                val selectedPresensi = spinner.selectedItem.toString()
                if(selectedPresensi == presensiOptions[1] || selectedPresensi == presensiOptions[2] || selectedPresensi == presensiOptions[3]) {
                    if(youcantseeme.text.toString() == "") {
                        // Menampilkan pesan kesalahan jika EditText keterangan kosong
                        Snackbar.make(root, "Mohon isi kolom keterangan", Snackbar.LENGTH_SHORT).setAnchorView(submit).show()
                    } else {
                        // Menampilkan pesan sukses jika presensi berhasil
                        Snackbar.make(root, "Presensi berhasil $selectedDate jam $selectedTime", Snackbar.LENGTH_SHORT).setAnchorView(submit).show()
                    }
                } else {
                    // Menampilkan pesan sukses jika presensi berhasil
                    Snackbar.make(root, "Presensi berhasil $selectedDate jam $selectedTime", Snackbar.LENGTH_SHORT).setAnchorView(submit).show()
                }
            }
        }
    }

    // Fungsi untuk menentukan format waktu yang dipilih
    private fun decideTime(hourOfDay:Int, minute:Int): String {
        var selectedTime = ""
        var minutes = minute.toString()
        if(minute < 10) {
            minutes = "0$minute"
        }
        if(hourOfDay < 12) {
            selectedTime = "$hourOfDay:$minutes AM"
        } else if(hourOfDay > 12) {
            selectedTime = "${hourOfDay % 12}:$minutes PM"
        }  else{
            if(minute > 0) {
                selectedTime = "${hourOfDay % 12}:$minutes PM"
            } else if(minute == 0) {
                selectedTime = "$hourOfDay:$minutes AM"
            }
        }
        return selectedTime
    }
}
