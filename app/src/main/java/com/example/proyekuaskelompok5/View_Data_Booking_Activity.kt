package com.example.proyekuaskelompok5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class View_Data_Booking_Activity : AppCompatActivity() {

    private lateinit var listview : ListView
    var arrayList_data = ArrayList<Booking_Model>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_data_booking)
        listview = findViewById(R.id.listViewdata_Booking)
        getData()

        listview.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(
                this@View_Data_Booking_Activity,
                Booking_Detail_With_Payment_Activity::class.java
            )
            intent.putExtra("kode_kamar", arrayList_data[position].kode_kamar)
            intent.putExtra("nama_pemesan", arrayList_data[position].nama_pemesan)
            intent.putExtra("kode_booking", arrayList_data[position].kode_booking)
            intent.putExtra("waktu_check_in", arrayList_data[position].waktu_check_in)
            intent.putExtra("waktu_check_out", arrayList_data[position].waktu_check_out)
            intent.putExtra("Image", arrayList_data[position].image_url)
            intent.putExtra("status_booking", arrayList_data[position].status_booking)
            startActivity(intent)
        }
    }
    private fun getData(){
        val url: String = AppConfig().IP_SERVER + "/hotel_web/view_data_booking.php"
        val stringRequest = object : StringRequest(
            Method.GET,url,
            Response.Listener { response ->
                Log.d("Response", "Server Response: $response")
                try {
                val jsonObj = JSONObject(response)
                Toast.makeText(this,jsonObj.getString("error_text"), Toast.LENGTH_SHORT).show()
                val jsonArray = jsonObj.getJSONArray("data")
                var bookingmodel: Booking_Model
                arrayList_data.clear()
                for (i in 0..jsonArray.length()-1) {


                        val item = jsonArray.getJSONObject(i)
                        bookingmodel = Booking_Model()
                        bookingmodel.kode_kamar = item.getString("kode_kamar")
                        bookingmodel.kode_booking = item.getString("kode_booking")
                        bookingmodel.nama_pemesan = item.getString("nama_pemesan")
                        bookingmodel.status_booking = item.getString("status_booking")
                        bookingmodel.waktu_check_in = item.getString("waktu_check_in")
                        bookingmodel.waktu_check_out = item.getString("waktu_check_out")
                        bookingmodel.image_url =
                            AppConfig().IP_SERVER + "/hotel_web/" + item.getString("foto_kamar")

                        arrayList_data.add(bookingmodel)

                }
                }catch (e: JSONException) {
                e.printStackTrace()
                Toast.makeText(this, "Invalid response from server", Toast.LENGTH_SHORT).show()
            }
                listview.adapter = BookingAdapter(this@View_Data_Booking_Activity, arrayList_data)
            },
            Response.ErrorListener { _ ->
                Toast.makeText(this,"Gagal Terhubung", Toast.LENGTH_SHORT).show()
            }
        ){}
        Volley.newRequestQueue(this).add(stringRequest)
    }
}