package com.example.proyekuaskelompok5

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONException
import org.json.JSONObject
import java.util.Calendar
import kotlin.random.Random

class Check_out_activity : AppCompatActivity() {
    private lateinit var editTextnama_pemesan   : EditText
    private lateinit var editTextkode_booking   : EditText
    private lateinit var editTextkode_kamar     : EditText
    private lateinit var editTexttipe_kamar     : EditText
    private lateinit var editTextjumlah_inap     : EditText
    private lateinit var editTextmetode_pembayaran     : EditText
    private lateinit var btnlanjut             : Button
    private lateinit var editTextcheck_in       : EditText
    private lateinit var editTextcheck_out      : EditText
    private lateinit var btndate_check_out      : Button
    private lateinit var btndate_check_in       : Button
    private var selected_item : String = ""
    private lateinit var button_back            : FloatingActionButton

    private lateinit var autoCompletetext: AutoCompleteTextView
    private lateinit var adapterItems: ArrayAdapter<String>
    val items = arrayOf("Tunai", "Debit", "E-Wallet")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check_out)

        editTextkode_booking= findViewById(R.id.editTextKodeBooking)
        editTextkode_kamar  = findViewById(R.id.editTextKodeKamar)
        editTexttipe_kamar = findViewById(R.id.editTextTipeKamar)
        editTextnama_pemesan = findViewById(R.id.editTextNama_Pemesan)
        editTextjumlah_inap = findViewById(R.id.editTextLama_menginap)
//        editTextmetode_pembayaran = findViewById(R.id.editTextMetode_pembayaran)
        editTextcheck_out   = findViewById(R.id.editText_Check_out_check_out)
        editTextcheck_in    = findViewById(R.id.editText_Check_in_check_out)
        btnlanjut           = findViewById(R.id.button_check_out)
        button_back = findViewById(R.id.floatingActionButton_back_check_out)

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

        autoCompletetext = findViewById(R.id.autoCompleteMetodePembayaran)
        adapterItems =  ArrayAdapter<String>(this, R.layout.single_data_dropdown, items)
        autoCompletetext.setAdapter(adapterItems)

        autoCompletetext.setOnItemClickListener { parent, view, position, id ->
            // Tindakan yang diambil ketika item dipilih
            // Misalnya: val selectedItem = parent.getItemAtPosition(position).toString()
            selected_item = parent.getItemAtPosition(position).toString()
            Toast.makeText(this, "items: "+selected_item, Toast.LENGTH_SHORT).show()
        }


        val bundle = intent.extras
        if (bundle!=null) { //ketika ada isi dari intent akan masuk ke dalam aksi if
            editTextkode_kamar.setText(bundle.getString("kode_kamar"))
            editTextkode_booking.setText(bundle.getString("kode_booking"))
            editTextnama_pemesan.setText(bundle.getString("nama_pemesan"))
            editTextcheck_in.setText(bundle.getString("waktu_check_in"))
            editTextcheck_out.setText(bundle.getString("waktu_check_out"))
        }

        get_Data()

        btnlanjut.setOnClickListener(){
            Kirim_Data()
        }

        button_back.setOnClickListener(){
            onBackPressed()
        }

    }

    private fun Kirim_Data(){

        if(editTextkode_booking.text.isNotEmpty() && selected_item.isNotEmpty()){
            val url: String = AppConfig().IP_SERVER + "/hotel_web/check_out.php"
            val stringRequest = object : StringRequest(
                Method.POST,url,
                Response.Listener { response ->
                    Log.d("Response", "Server Response: $response")
                    try {
                        val jsonObj = JSONObject(response)
                        Toast.makeText(this, jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@Check_out_activity, Payment_activity::class.java)

                        intent.putExtra("kode_kamar", editTextkode_kamar.text.toString())
                        intent.putExtra("nama_pemesan", editTextnama_pemesan.text.toString())
                        intent.putExtra("kode_booking", editTextkode_booking.text.toString())
                        intent.putExtra("tipe_kamar", editTexttipe_kamar.text.toString())
                        intent.putExtra("waktu_check_in", editTextcheck_in.text.toString())
                        intent.putExtra("waktu_check_out", editTextcheck_out.text.toString())
                        intent.putExtra("jumlah_inap", editTextjumlah_inap.text.toString())
                        intent.putExtra("metode_payment", selected_item)
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
                    params["metode_payment"] = selected_item.toString()

                    return params
                }
            }
            Volley.newRequestQueue(this).add(stringRequest)
        }
        else{
            Toast.makeText(this, "Data yang dimasukkan belum lengkap", Toast.LENGTH_LONG).show()
        }
    }

    private fun get_Data(){

        if(editTextkode_booking.text.isNotEmpty()){
            val url: String = AppConfig().IP_SERVER + "/hotel_web/check_out.php"
            val stringRequest = object : StringRequest(
                Method.POST, url,
                Response.Listener { response ->
                    Log.d("Response", "Server Response: $response")
                    try {
                        val jsonObj = JSONObject(response)
                        Log.d("Response", "Parsed JSON: $jsonObj")

                        val lamaInap = jsonObj.getInt("lama_inap")
                        val dataArray = jsonObj.getJSONArray("data")
                        val tipeKamar = dataArray.getJSONObject(0).getString("tipe_kamar")

                        editTexttipe_kamar.setText(tipeKamar)
                        editTextjumlah_inap.setText(lamaInap.toString())

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
                    params["metode_payment"] = selected_item.toString()
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