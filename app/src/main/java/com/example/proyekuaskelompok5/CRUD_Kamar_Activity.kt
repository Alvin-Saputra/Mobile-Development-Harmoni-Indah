package com.example.proyekuaskelompok5

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import android.util.Base64
import android.util.FloatProperty
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONException

class CRUD_Kamar_Activity : AppCompatActivity() {
    private lateinit var editTextkode_kamar       : EditText
    private lateinit var editTextharga_per_malam   : EditText
    private lateinit var action         : String
    private var id        = 0
    private var resId     = 0
    private lateinit var selected_item : String
    private lateinit var bitmap         : Bitmap
    private lateinit var gambar_kamar   : ImageView
    private lateinit var btnsubmit      : Button
    private lateinit var btnedit        : Button
    private lateinit var btndelete      : Button
    private lateinit var btnbooking     : Button
    private lateinit var button_back    : FloatingActionButton

    private lateinit var autoCompletetext: AutoCompleteTextView
    private lateinit var adapterItems: ArrayAdapter<String>

    val items = arrayOf("Standard", "Superior", "Deluxe", "Suite")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crud_kamar)


        editTextkode_kamar = findViewById(R.id.editTextKodeKamar)
        editTextharga_per_malam = findViewById(R.id.editTextHarga_Per_Malam)
        gambar_kamar = findViewById(R.id.gambar_kamar)
        btnsubmit = findViewById(R.id.button_submit_kamar)
        btndelete = findViewById(R.id.button_delete)
        btnedit   =  findViewById(R.id.button_edit)
        btnbooking = findViewById(R.id.button_booking)
        button_back = findViewById(R.id.floatingActionButton_back_crud_kamar)

        autoCompletetext = findViewById(R.id.auto_complete_txt)
        adapterItems =  ArrayAdapter<String>(this, R.layout.single_data_dropdown, items)
        autoCompletetext.setAdapter(adapterItems)



        autoCompletetext.setOnItemClickListener { parent, view, position, id ->
            // Tindakan yang diambil ketika item dipilih
            // Misalnya: val selectedItem = parent.getItemAtPosition(position).toString()
            selected_item = parent.getItemAtPosition(position).toString()
            Toast.makeText(this, "items: "+selected_item, Toast.LENGTH_SHORT).show()
        }

        imagePick()

        val bundle = intent.extras
        if (bundle!=null) { //ketika ada isi dari intent akan masuk ke dalam aksi if
            editTextkode_kamar.setText(bundle.getString("kode_kamar"))
            editTextharga_per_malam.setText(bundle.getString("harga_per_malam"))

            editTextkode_kamar.isFocusable = false
            editTextkode_kamar.isFocusableInTouchMode = false
//            val adapter = ArrayAdapter<String>(this, R.layout.single_data_dropdown, items)
//            autoCompletetext.setAdapter(adapterItems)
//
//            autoCompletetext.setText(bundle.getString("tipe_kamar"))
            adapterItems =  ArrayAdapter<String>(this, R.layout.single_data_dropdown, items)
            var itemPosition = adapterItems.getPosition(bundle.getString("tipe_kamar"))
            autoCompletetext.setText(adapterItems.getItem(itemPosition))
            autoCompletetext.setAdapter(adapterItems)


            val imageUrl = bundle.getString("Image")

            // Menggunakan Glide untuk menampilkan gambar
            Glide.with(this)
                .load(imageUrl)
                .into(gambar_kamar)
            btnsubmit.visibility = View.GONE

        }
        else { //sedangkan jika tidak ada data dari intent maka akan masuk ke dalam aski else
            btndelete.visibility   = View.GONE
            btnedit.visibility = View.GONE
            btnbooking.visibility = View.GONE
        }

        btnsubmit.setOnClickListener(){
            action = "add"
            kirim_data()
        }

        btndelete.setOnClickListener(){
            action = "delete"
            kirim_data_delete()
        }

        btnedit.setOnClickListener(){
            action = "update"
            kirim_data_update()
        }

        btnbooking.setOnClickListener(){
            val intent = Intent(this@CRUD_Kamar_Activity, BookingActivity::class.java)
            intent.putExtra("kode_kamar", editTextkode_kamar.text.toString())
            intent.putExtra("tipe_kamar", autoCompletetext.text.toString())
            startActivity(intent)
            finish()
        }

        button_back.setOnClickListener(){
            val intent = Intent(this@CRUD_Kamar_Activity, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun imagePick() {
        val activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data!!
                val uri = data.data
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    gambar_kamar.setImageBitmap(bitmap)
                    resId = 1
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        gambar_kamar.setOnClickListener {
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            activityResultLauncher.launch(intent)
        }
    }

    private fun kirim_data(){
        if(resId==1){
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
            val ImageViewToByte = byteArrayOutputStream.toByteArray()
            val EncodedImage    = Base64.encodeToString(ImageViewToByte, Base64.DEFAULT)

            Log.d("Response", selected_item)

            val url: String = AppConfig().IP_SERVER + "/hotel_web/send_data_kamar.php"
            val stringRequest = object : StringRequest(
                Method.POST,url,
                Response.Listener { response ->
                    Log.d("Response", "Server Response: $response")
                    try {
                        val jsonObj = JSONObject(response)
                        Toast.makeText(this, jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@CRUD_Kamar_Activity, MainActivity::class.java)
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
                    params["kode_kamar"]      = editTextkode_kamar.text.toString()
                    params["harga_per_malam"] = editTextharga_per_malam.text.toString()
                    params["image"]           = EncodedImage
                    params["tipe_kamar"]      = selected_item.toString()
                    params["action"]          = action
                    return params
                }
            }
            Volley.newRequestQueue(this).add(stringRequest)
        }
        else{
            Toast.makeText(this, "Anda Belum Memasukkan Foto Kamar", Toast.LENGTH_LONG).show()
        }
    }

    private fun kirim_data_delete(){

            val url: String = AppConfig().IP_SERVER + "/hotel_web/delete_data_kamar.php"
            val stringRequest = object : StringRequest(
                Method.POST,url,
                Response.Listener { response ->
                    Log.d("Response", "Server Response: $response")
                    try {
                        val jsonObj = JSONObject(response)
                        Toast.makeText(this, jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@CRUD_Kamar_Activity, View_Data_CRUD_Kamar_Activity::class.java)
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
                    params["kode_kamar"]      = editTextkode_kamar.text.toString()
//                    params["harga_per_malam"] = ""
//                    params["tipe_kamar"]      = ""
//                    params["action"]          = action
                    return params
                }
            }
            Volley.newRequestQueue(this).add(stringRequest)
    }

    private fun kirim_data_update(){
        if(resId==1){
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
            val ImageViewToByte = byteArrayOutputStream.toByteArray()
            val EncodedImage    = Base64.encodeToString(ImageViewToByte, Base64.DEFAULT)


            val url: String = AppConfig().IP_SERVER + "/hotel_web/send_data_kamar.php"
            val stringRequest = object : StringRequest(
                Method.POST,url,
                Response.Listener { response ->
                    Log.d("Response", "Server Response: $response")
                    try {
                        val jsonObj = JSONObject(response)
                        Toast.makeText(this, jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@CRUD_Kamar_Activity, View_Data_CRUD_Kamar_Activity::class.java)
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
                    params["kode_kamar"]      = editTextkode_kamar.text.toString()
                    params["harga_per_malam"] = editTextharga_per_malam.text.toString()
                    params["image"]           = EncodedImage
                    params["tipe_kamar"]      = autoCompletetext.text.toString()
                    params["action"]          = action
                    return params
                }
            }
            Volley.newRequestQueue(this).add(stringRequest)
        }

        else{
           val EncodedImage = ""

            val url: String = AppConfig().IP_SERVER + "/hotel_web/send_data_kamar.php"
            val stringRequest = object : StringRequest(
                Method.POST,url,
                Response.Listener { response ->
                    Log.d("Response", "Server Response: $response")
                    try {
                        val jsonObj = JSONObject(response)
                        Toast.makeText(this, jsonObj.getString("message"), Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@CRUD_Kamar_Activity, MainActivity::class.java)
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
                    params["kode_kamar"]      = editTextkode_kamar.text.toString()
                    params["harga_per_malam"] = editTextharga_per_malam.text.toString()
                    params["image"]           = EncodedImage
                    params["tipe_kamar"]      = autoCompletetext.text.toString()
                    params["action"]          = action
                    return params
                }
            }
            Volley.newRequestQueue(this).add(stringRequest)

        }

    }


}