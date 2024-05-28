package com.example.proyekuaskelompok5

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
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
import java.io.ByteArrayOutputStream
import kotlin.random.Random

class Payment_Detail_Activity : AppCompatActivity() {
    private lateinit var editText_kode_payment   : EditText
    private lateinit var editText_kode_booking   : EditText
    private lateinit var editText_metode_payment    : EditText
    private lateinit var editText_total_bayar     : EditText
    private lateinit var btndelete_payment             : Button
    private lateinit var button_back            : FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment_detail)

        editText_kode_payment = findViewById(R.id.editTextKodePayment_detail)
        editText_kode_booking = findViewById(R.id.editTextBooking_Detail)
        editText_metode_payment = findViewById(R.id.editTextMetode_Payment)
        editText_total_bayar = findViewById(R.id.editTextTotal_Payment)
        btndelete_payment = findViewById(R.id.button_delete_payment)
        button_back = findViewById(R.id.floatingActionButton_back_payment_detail)
        val bundle = intent.extras
        if (bundle!=null) { //ketika ada isi dari intent akan masuk ke dalam aksi if
            editText_kode_booking.setText(bundle.getString("kode_booking"))

            val status_booking = bundle.getString("status_booking")
            if(status_booking == "Selesai"){
//                btndelete_payment.visibility   = View.GONE
            }
        }

        get_Data()

        btndelete_payment.setOnClickListener(){
            showConfirmationDialog()
        }

        button_back.setOnClickListener(){
            onBackPressed()
        }
    }

    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Konfirmasi Hapus")
        builder.setMessage("Apakah Anda yakin ingin menghapus data pembayaran ini? Data booking juga akan dihapus bersama dengan data pembayaran ini.")

        builder.setNegativeButton("No") { dialog, which ->
            // User menekan tombol "No", tutup kotak dialog
            dialog.dismiss()
        }

        builder.setPositiveButton("Yes") { dialog, which ->
            // User menekan tombol "Yes", jalankan fungsi kirim_data_delete()
            kirim_data_delete()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun get_Data(){
            val url: String = AppConfig().IP_SERVER + "/hotel_web/view_data_payment.php"
            val stringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    Log.d("Response", "Server Response: $response")
                    try {
                        val jsonObj = JSONObject(response)
                        Log.d("Response", "Parsed JSON: $jsonObj")

                        val dataArray = jsonObj.getJSONArray("data")
                        val kode_payment = dataArray.getJSONObject(0).getString("kode_payment")
                        val metode_payment = dataArray.getJSONObject(0).getString("metode_payment")
                        val total_payment = dataArray.getJSONObject(0).getString("total_payment")

                        editText_kode_payment.setText(kode_payment)
                        editText_metode_payment.setText(metode_payment)
                        editText_total_bayar.setText(total_payment)

                        Toast.makeText(this, jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                Response.ErrorListener { error ->
                    Log.e("Volley Error", "Error: ${error.message}")
                    Toast.makeText(this, "Gagal Terhubung", Toast.LENGTH_SHORT).show()
                }
            ){
                override fun getParams(): HashMap<String, String>{
                    val params = HashMap<String, String>()
                    params["kode_booking"] = editText_kode_booking.text.toString()
                    return params
                }
            }
            Volley.newRequestQueue(this).add(stringRequest)

    }

    private fun kirim_data_delete(){

            val url: String = AppConfig().IP_SERVER + "/hotel_web/delete_data_payment.php"
            val stringRequest = object : StringRequest(
                Method.POST,url,
                Response.Listener { response ->
                    Log.d("Response", "Server Response: $response")
                    try {
                        val jsonObj = JSONObject(response)
                        Toast.makeText(this, jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Payment_Detail_Activity, View_Data_Booking_Activity::class.java)
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
                    params["kode_booking"]    = editText_kode_booking.text.toString()
                    params["kode_payment"] = editText_kode_payment.text.toString()
                    return params
                }
            }
            Volley.newRequestQueue(this).add(stringRequest)

    }
}