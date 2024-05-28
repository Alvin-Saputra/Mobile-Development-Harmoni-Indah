package com.example.proyekuaskelompok5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class View_Data_CRUD_Kamar_Activity : AppCompatActivity() {

    private lateinit var listview : ListView
    var arrayList_data = ArrayList<Kamar_model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_data_crud_kamar)

        listview = findViewById(R.id.listViewdata)

        getData()
        listview.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this@View_Data_CRUD_Kamar_Activity, CRUD_Kamar_Activity::class.java)
            intent.putExtra("kode_kamar", arrayList_data[position].kode_kamar)
            intent.putExtra("harga_per_malam", arrayList_data[position].harga_per_malam)
            intent.putExtra("tipe_kamar", arrayList_data[position].tipe_kamar)
            intent.putExtra("Image", arrayList_data[position].image_url)
            startActivity(intent)
            finish()

//            if(arrayList_data[position].image_url == "Tersedia"){
//                startActivity(intent)
//                finish()
//            }
//            else{
//                Toast.makeText(this,"Tidak bisa Booking", Toast.LENGTH_SHORT).show()
//            }

        }
    }

    private fun getData(){
        val url: String = AppConfig().IP_SERVER + "/hotel_web/view_data_kamar.php"
        val stringRequest = object : StringRequest(
            Method.GET,url,
            Response.Listener { response ->
                val jsonObj = JSONObject(response)
                Toast.makeText(this,jsonObj.getString("error_text"), Toast.LENGTH_SHORT).show()
                val jsonArray = jsonObj.getJSONArray("data")
                var kamarmodel: Kamar_model
                arrayList_data.clear()
                for (i in 0..jsonArray.length()-1) {
                    val item = jsonArray.getJSONObject(i)
                    kamarmodel = Kamar_model()
                    kamarmodel.kode_kamar = item.getString("kode_kamar")
                    kamarmodel.tipe_kamar = item.getString("tipe_kamar")
                    kamarmodel.harga_per_malam = item.getString("harga_per_malam")
                    kamarmodel.image_url = AppConfig().IP_SERVER+"/hotel_web/" + item.getString("foto_kamar")
                    kamarmodel.status_kamar = item.getString("status_kamar")
                    arrayList_data.add(kamarmodel)
                }
                listview.adapter = KamarAdapter(this@View_Data_CRUD_Kamar_Activity, arrayList_data)
            },
            Response.ErrorListener { _ ->
                Toast.makeText(this,"Gagal Terhubung", Toast.LENGTH_SHORT).show()
            }
        ){}
        Volley.newRequestQueue(this).add(stringRequest)
    }
}