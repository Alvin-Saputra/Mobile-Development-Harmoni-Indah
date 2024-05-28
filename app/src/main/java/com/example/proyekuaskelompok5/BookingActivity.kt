package com.example.proyekuaskelompok5

import android.app.DatePickerDialog
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random

class BookingActivity : AppCompatActivity() {
    private var date_check = ""
    private lateinit var editTextnama_pemesan   : EditText
    private lateinit var editTextkode_booking   : EditText
    private lateinit var editTextkode_kamar     : EditText
    private lateinit var editTexttipe_kamar     : EditText
    private lateinit var editTextcheck_in       : EditText
    private lateinit var editTextcheck_out      : EditText
    private lateinit var btndate_check_out      : Button
    private lateinit var btndate_check_in       : Button
    private lateinit var btnsubmit              : Button
    private lateinit var button_back            : FloatingActionButton
    private var calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        editTextkode_booking= findViewById(R.id.editTextKodeBooking)
        editTextkode_kamar  = findViewById(R.id.editTextKodeKamar)
        editTexttipe_kamar = findViewById(R.id.editTextTipeKamar)
        editTextnama_pemesan = findViewById(R.id.editTextNama_Pemesan)
        btndate_check_in    = findViewById(R.id.button_date_check_in)
        btndate_check_out   = findViewById(R.id.button_date_check_out)
        editTextcheck_out   = findViewById(R.id.editText_Check_out)
        editTextcheck_in    = findViewById(R.id.editText_Check_in)
        btnsubmit           = findViewById(R.id.button_submit_booking)

        button_back = findViewById(R.id.floatingActionButton_back_booking)

        editTextkode_booking.isFocusable = false
        editTextkode_booking.isFocusableInTouchMode = false

        editTextkode_kamar.isFocusable = false
        editTextkode_kamar.isFocusableInTouchMode = false

        editTexttipe_kamar.isFocusable = false
        editTexttipe_kamar.isFocusableInTouchMode = false

//        val myCalendar = Calendar.getInstance()
//
//                         val datePicker = DatePickerDialog.on{view, year, month, dayOfMonth ->
//            myCalendar.set(Calendar.YEAR, year)
//            myCalendar.set(Calendar.MONTH, month)
//            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
//            updatelable(myCalendar)
//
//        }


        val bundle = intent.extras
        if (bundle!=null) { //ketika ada isi dari intent akan masuk ke dalam aksi if
            editTextkode_kamar.setText(bundle.getString("kode_kamar"))
            editTexttipe_kamar.setText(bundle.getString("tipe_kamar"))
            val randomNumber = Random.nextInt(1000, 9999)
            var kode_booking = "BOOK_" + editTextkode_kamar.text.toString() + "_$randomNumber"

            editTextkode_booking.setText("$kode_booking")
        }



        btndate_check_in.setOnClickListener(){
            date_check = "in"
            showDatePicker()
        }

        btndate_check_out.setOnClickListener(){
            date_check = "out"
            showDatePicker()
        }

        btnsubmit.setOnClickListener(){
            Kirim_Data()
        }

        button_back.setOnClickListener(){
            onBackPressed()
        }

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
            editTextcheck_in.setText(store_date)
        }
        else if (date_check == "out"){
            editTextcheck_out.setText(store_date)
        }

    }

    private fun Kirim_Data(){

        if(editTextkode_booking.text.isNotEmpty() && editTextcheck_in.text.isNotEmpty() && editTextcheck_out.text.isNotEmpty() && editTextnama_pemesan.text.isNotEmpty()){
            val url: String = AppConfig().IP_SERVER + "/hotel_web/send_data_booking.php"
            val stringRequest = object : StringRequest(
                Method.POST,url,
                Response.Listener { response ->
                    Log.d("Response", "Server Response: $response")
                    try {
                        val jsonObj = JSONObject(response)
                        Toast.makeText(this, jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@BookingActivity, MainActivity::class.java)
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
                    params["nama_pemesan"]    = editTextnama_pemesan.text.toString()
                    params["kode_booking"]    = editTextkode_booking.text.toString()
                    params["kode_kamar"]      = editTextkode_kamar.text.toString()
                    params["date_check_in"]   = editTextcheck_in.text.toString()
                    params["date_check_out"]  = editTextcheck_out.text.toString()
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