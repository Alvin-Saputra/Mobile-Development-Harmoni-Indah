package com.example.proyekuaskelompok5

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class Booking_Detail_With_Payment_Activity : AppCompatActivity() {
    private var date_check = ""
    private var status_book = ""
    private lateinit var editTxt_nama_pemesan : EditText
    private lateinit var editTxt_kode_booking : EditText
    private lateinit var editTxt_kode_kamar   : EditText
    private lateinit var editTxt_waktu_check_in : EditText
    private lateinit var editTxt_waktu_check_out : EditText
    private lateinit var btndate_check_out      : Button
    private lateinit var btndate_check_in       : Button
    private lateinit var btnupdate              : Button
    private lateinit var btndelete              : Button
    private lateinit var btnpayment_detail      : Button
    private lateinit var button_back            : FloatingActionButton
    private var calendar: Calendar = Calendar.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_detail_with_payment)

        editTxt_nama_pemesan = findViewById(R.id.editTextNama_Pemesan_detail)
        editTxt_kode_booking = findViewById(R.id.editTextKodeBooking_detail)
        editTxt_kode_kamar = findViewById(R.id.editTextKodeKamar_detail)
        editTxt_waktu_check_in = findViewById(R.id.editText_Check_in_detail)
        editTxt_waktu_check_out = findViewById(R.id.editText_Check_out_detail)
        btndate_check_in = findViewById(R.id.button_date_check_in_detail)
        btndate_check_out = findViewById(R.id.button_date_check_out_detail)
        btnupdate = findViewById(R.id.button_update_booking)
        btndelete = findViewById(R.id.button_delete_booking)
        btnpayment_detail = findViewById(R.id.button_detail_payment)
        button_back = findViewById(R.id.floatingActionButton_back_detail_booking)

        editTxt_kode_booking.isFocusable = false
        editTxt_kode_booking.isFocusableInTouchMode = false

        editTxt_kode_kamar.isFocusable = false
        editTxt_kode_kamar.isFocusableInTouchMode = false

        val bundle = intent.extras
        if (bundle != null) { //ketika ada isi dari intent akan masuk ke dalam aksi if
            editTxt_nama_pemesan.setText(bundle.getString("nama_pemesan"))
            editTxt_kode_booking.setText(bundle.getString("kode_booking"))
            editTxt_kode_kamar.setText(bundle.getString("kode_kamar"))
            editTxt_waktu_check_in.setText(bundle.getString("waktu_check_in"))
            editTxt_waktu_check_out.setText(bundle.getString("waktu_check_out"))

            val status_booking = bundle.getString("status_booking")
            status_book = status_booking.toString()
            Log.d("StatusBooking", "Status Booking: $status_booking")
            if(status_booking == "Selesai"){
//                btndelete.visibility   = View.GONE
                btnupdate.visibility = View.GONE
            }
        }

        btndate_check_in.setOnClickListener(){
            date_check = "in"
            showDatePicker()
        }

        btndate_check_out.setOnClickListener(){
            date_check = "out"
            showDatePicker()
        }

        btnupdate.setOnClickListener(){
            Kirim_Data_Update()
        }

        btndelete.setOnClickListener(){
            showConfirmationDialog()

        }

        button_back.setOnClickListener(){
            onBackPressed()
        }

        btnpayment_detail.setOnClickListener(){
            val intent = Intent(this@Booking_Detail_With_Payment_Activity, Payment_Detail_Activity::class.java)
            intent.putExtra("kode_booking", editTxt_kode_booking.text.toString())
            intent.putExtra("status_booking", status_book)
            startActivity(intent)
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi Hapus")
        builder.setMessage("Apakah Anda yakin ingin menghapus data booking ini? Data pembayaran juga akan dihapus bersama dengan data booking ini.")

        builder.setNegativeButton("No") { dialog, which ->
            // User menekan tombol "No", tutup kotak dialog
            dialog.dismiss()
        }

        builder.setPositiveButton("Yes") { dialog, which ->
            // User menekan tombol "Yes", jalankan fungsi kirim_data_delete()
            Kirim_Data_Delete()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun updateDateInView() {
        var store_date = ""
        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        store_date = sdf.format(calendar.time)
        if(date_check == "in"){
            editTxt_waktu_check_in.setText(store_date)
        }
        else if (date_check == "out"){
            editTxt_waktu_check_out.setText(store_date)
        }

    }

    private fun Kirim_Data_Update(){

        if(editTxt_nama_pemesan.text.isNotEmpty() || editTxt_kode_booking.text.isNotEmpty() || editTxt_waktu_check_in.text.isNotEmpty() || editTxt_waktu_check_out.text.isNotEmpty()){
            val url: String = AppConfig().IP_SERVER + "/hotel_web/update_data_booking.php"
            val stringRequest = object : StringRequest(
                Method.POST,url,
                Response.Listener { response ->
                    Log.d("Response", "Server Response: $response")
                    try {
                        val jsonObj = JSONObject(response)
                        Toast.makeText(this, jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Booking_Detail_With_Payment_Activity, View_Data_Booking_Activity::class.java)
                        startActivity(intent)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Invalid response from server", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { _ ->

                    Toast.makeText(this,"Gagal Terhubung", Toast.LENGTH_SHORT).show()
                }
            ){
                override fun getParams(): HashMap<String,String>{
                    val params = HashMap<String,String>()
                    params["nama_pemesan"]    = editTxt_nama_pemesan.text.toString()
                    params["kode_kamar"]      = editTxt_kode_kamar.text.toString()
                    params["kode_booking"]    = editTxt_kode_booking.text.toString()
                    params["date_check_in"]   = editTxt_waktu_check_in.text.toString()
                    params["date_check_out"]  = editTxt_waktu_check_out.text.toString()
                    return params
                }
            }
            Volley.newRequestQueue(this).add(stringRequest)
        }
        else{
            Toast.makeText(this, "Data yang dimasukkan belum lengkap", Toast.LENGTH_LONG).show()
        }
    }


    private fun Kirim_Data_Delete(){
            val url: String = AppConfig().IP_SERVER + "/hotel_web/delete_data_booking.php"
            val stringRequest = object : StringRequest(
                Method.POST,url,
                Response.Listener { response ->
                    Log.d("Response", "Server Response: $response")
                    try {
                        val jsonObj = JSONObject(response)
                        Toast.makeText(this, jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Booking_Detail_With_Payment_Activity, View_Data_Booking_Activity::class.java)
                        startActivity(intent)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Toast.makeText(this, "Invalid response from server", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { _ ->

                    Toast.makeText(this,"Gagal Terhubung", Toast.LENGTH_SHORT).show()
                }
            ){
                override fun getParams(): HashMap<String,String>{
                    val params = HashMap<String,String>()
                    params["kode_booking"]    = editTxt_kode_booking.text.toString()
                    params["kode_kamar"]      = editTxt_kode_kamar.text.toString()
                    return params
                }
            }
            Volley.newRequestQueue(this).add(stringRequest)
    }
}