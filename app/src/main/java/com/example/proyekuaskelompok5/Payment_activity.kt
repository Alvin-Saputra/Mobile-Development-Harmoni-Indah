package com.example.proyekuaskelompok5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONException
import org.json.JSONObject

class Payment_activity : AppCompatActivity() {

    private lateinit var editTextnama_pemesan   : EditText
    private lateinit var editTextkode_booking   : EditText
    private lateinit var editTextkode_kamar     : EditText
    private lateinit var editTexttipe_kamar     : EditText
    private lateinit var editTextjumlah_inap     : EditText
    private lateinit var editTextmetode_pembayaran     : EditText
    private lateinit var btnbayar             : Button
    private lateinit var editTextcheck_in       : EditText
    private lateinit var editTextcheck_out      : EditText
    private lateinit var editTexttotal_harga    : EditText
    private lateinit var editTextharga_per_malam: EditText
    private lateinit var button_back            : FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)


        editTextkode_booking= findViewById(R.id.editTextKodeBooking_payment)
        editTextkode_kamar  = findViewById(R.id.editTextKodeKamar)
        editTexttipe_kamar = findViewById(R.id.editTextTipeKamar)
        editTextnama_pemesan = findViewById(R.id.editTextNama_Pemesan)
        editTextjumlah_inap = findViewById(R.id.editTextLama_menginap)
        editTextmetode_pembayaran = findViewById(R.id.editTextMetode_pembayaran)
        editTextcheck_out   = findViewById(R.id.editText_Check_out_check_out)
        editTextcheck_in    = findViewById(R.id.editText_Check_in_check_out)
        btnbayar           = findViewById(R.id.button_bayar)
        editTexttotal_harga = findViewById(R.id.editTextTotal_bayar)
        editTextharga_per_malam = findViewById(R.id.editTextharga_per_malam)
        button_back = findViewById(R.id.floatingActionButton_back_payment)

        editTextkode_booking.isFocusable = false
        editTextkode_booking.isFocusableInTouchMode = false

        editTextkode_kamar.isFocusable = false
        editTextkode_kamar.isFocusableInTouchMode = false

        editTextnama_pemesan.isFocusable = false
        editTextnama_pemesan.isFocusableInTouchMode = false

        editTexttipe_kamar.isFocusable = false
        editTexttipe_kamar.isFocusableInTouchMode = false

        editTextcheck_out.isFocusable = false
        editTextcheck_out.isFocusableInTouchMode = false

        editTextcheck_in.isFocusable = false
        editTextcheck_in.isFocusableInTouchMode = false

        editTextjumlah_inap.isFocusable = false
        editTextjumlah_inap.isFocusableInTouchMode = false

        editTexttotal_harga.isFocusable = false
        editTexttotal_harga.isFocusableInTouchMode = false

        editTextharga_per_malam.isFocusable = false
        editTextharga_per_malam.isFocusableInTouchMode = false

        editTextmetode_pembayaran.isFocusable = false
        editTextmetode_pembayaran.isFocusableInTouchMode = false

        val bundle = intent.extras
        if (bundle!=null) {
            editTextkode_kamar.setText(bundle.getString("kode_kamar"))
            editTextkode_booking.setText(bundle.getString("kode_booking"))
            editTextnama_pemesan.setText(bundle.getString("nama_pemesan"))
            editTextcheck_in.setText(bundle.getString("waktu_check_in"))
            editTextcheck_out.setText(bundle.getString("waktu_check_out"))
            editTextjumlah_inap.setText(bundle.getString("jumlah_inap"))
            editTextmetode_pembayaran.setText(bundle.getString("metode_payment"))
            editTexttipe_kamar.setText(bundle.getString("tipe_kamar"))
        }

        get_Data()

        btnbayar.setOnClickListener(){
            kirim_data()
        }

        button_back.setOnClickListener(){
            onBackPressed()
        }
    }

    private fun get_Data(){

        if(editTextkode_booking.text.isNotEmpty()){
            val url: String = AppConfig().IP_SERVER + "/hotel_web/payment.php"
            val stringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    Log.d("Response", "Server Response: $response")
                    try {
                        val jsonObj = JSONObject(response)
                        Log.d("Response", "Parsed JSON: $jsonObj")

                        val total_payment = jsonObj.getInt("total_payment")
                        val harga_per_malam = jsonObj.getInt("harga_per_malam")

                        editTextharga_per_malam.setText(String.format("Rp%,d", harga_per_malam));
                        editTexttotal_harga.setText(String.format("Rp%,d", total_payment));

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
                    params["kode_kamar"] = editTextkode_kamar.text.toString()
                    params["kode_booking"] = editTextkode_booking.text.toString()
                    params["nama_pemesan"] = editTextnama_pemesan.text.toString()
                    params["date_check_in"] = editTextcheck_in.text.toString()
                    params["date_check_out"] = editTextcheck_out.text.toString()
                    params["metode_payment"] = editTextmetode_pembayaran.text.toString()
                    params["lama_inap"] = editTextjumlah_inap.text.toString()
                    params["tipe_kamar"] = editTexttipe_kamar.text.toString()
                    return params
                }
            }

            Volley.newRequestQueue(this).add(stringRequest)
        }
        else{
            Toast.makeText(this, "Data yang dimasukkan belum lengkap", Toast.LENGTH_LONG).show()
        }
    }

    private fun kirim_data(){

        if(editTextkode_booking.text.isNotEmpty()){
            val url: String = AppConfig().IP_SERVER + "/hotel_web/finished_payment.php"
            val stringRequest = object : StringRequest(
                Method.POST,url,
                Response.Listener { response ->
                    Log.d("Response", "Server Response: $response")
                    try {
                        val jsonObj = JSONObject(response)
                        Toast.makeText(this, jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Payment_activity, MainActivity::class.java)
                        startActivity(intent)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("JSONException", "Exception: ${e.message}")
                        Toast.makeText(this, "Invalid response from server", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { _ ->

                    Toast.makeText(this,"Gagal Terhubung", Toast.LENGTH_SHORT).show()
                }
            ){
                override fun getParams(): HashMap<String,String>{
                    val params = HashMap<String,String>()
                    params["kode_kamar"] = editTextkode_kamar.text.toString()
                    params["kode_booking"] = editTextkode_booking.text.toString()
                    params["nama_pemesan"] = editTextnama_pemesan.text.toString()
                    params["date_check_in"] = editTextcheck_in.text.toString()
                    params["date_check_out"] = editTextcheck_out.text.toString()
                    params["metode_payment"] = editTextmetode_pembayaran.text.toString()
                    params["lama_inap"] = editTextjumlah_inap.text.toString()
                    params["tipe_kamar"] = editTexttipe_kamar.text.toString()
                    return params
                }
            }
            Volley.newRequestQueue(this).add(stringRequest)
        }
        else{
            Toast.makeText(this, "Data yang dimasukkan belum lengkap", Toast.LENGTH_LONG).show()
        }
    }

}