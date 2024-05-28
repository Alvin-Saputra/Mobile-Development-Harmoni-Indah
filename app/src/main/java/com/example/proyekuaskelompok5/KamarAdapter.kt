package com.example.proyekuaskelompok5

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class KamarAdapter(private var context: Context, arraylist_data: ArrayList<Kamar_model>) : BaseAdapter() {
    private var arraylist_data: ArrayList<Kamar_model> = arraylist_data
    private var inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return arraylist_data.size
    }

    override fun getItem(position: Int): Any {
        return arraylist_data[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var vi: View? = convertView
        if (convertView == null)
            vi = inflater.inflate(R.layout.single_data, null)

        val textViewkode_kamar      : TextView  = vi!!.findViewById(R.id.txt_kode_kamar)
        val textViewharga_per_malam : TextView  = vi.findViewById(R.id.txt_harga_per_malam)
        val textViewtipe_kamar      : TextView  = vi.findViewById(R.id.txt_tipe_kamar)
        val foto_kamar              : ImageView = vi.findViewById(R.id.foto_kamar)
        val textViewstatus_kamar    : TextView  = vi.findViewById(R.id.txt_status_kamar)
        val button_booking          : Button    =  vi.findViewById(R.id.btn_booking)
        val kamar_model = arraylist_data[position]

        textViewkode_kamar.text  = kamar_model.kode_kamar
        textViewharga_per_malam.text = "Rp %,.0f".format(kamar_model.harga_per_malam.toFloat())
        textViewtipe_kamar.text = kamar_model.tipe_kamar
        Glide.with(vi.context)
            .load(kamar_model.image_url)
            .into(foto_kamar)

        textViewstatus_kamar.text = kamar_model.status_kamar

        button_booking.setOnClickListener(){
            val intent = Intent(context, BookingActivity::class.java)
            intent.putExtra("kode_kamar", kamar_model.kode_kamar)
            intent.putExtra("harga_per_malam", kamar_model.harga_per_malam.toFloat())
            intent.putExtra("tipe_kamar", kamar_model.tipe_kamar)
            intent.putExtra("Image", kamar_model.image_url)
            intent.putExtra("status_kamar", kamar_model.status_kamar)

            if(kamar_model.status_kamar == "Tersedia"){
                context.startActivity(intent)
            }

            else{
                Toast.makeText(context,"Tidak bisa dapat membooking kamar. Kamar tidak tersedia", Toast.LENGTH_LONG).show()
            }

//            finish()
        }
        return vi
    }
}