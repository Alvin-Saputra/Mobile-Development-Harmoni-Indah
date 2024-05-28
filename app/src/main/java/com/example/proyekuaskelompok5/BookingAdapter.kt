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

class BookingAdapter(private var context: Context, arraylist_data: ArrayList<Booking_Model>) : BaseAdapter() {
    private var arraylist_data: ArrayList<Booking_Model> = arraylist_data
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
            vi = inflater.inflate(R.layout.single_data_booking, null)

        val textViewkode_kamar      : TextView  = vi!!.findViewById(R.id.txt_kode_kamar_book)
        val textViewnama_pemesan    : TextView  = vi.findViewById(R.id.txt_nama_pemesan)
        val textViewkode_booking      : TextView  = vi.findViewById(R.id.txt_kode_booking)
        val foto_kamar              : ImageView = vi.findViewById(R.id.foto_kamar_book)
        val textViewstatus_booking    : TextView  = vi.findViewById(R.id.txt_status_booking)
        val textViewaktu_check_in     : TextView    = vi.findViewById(R.id.txt_waktu_check_in)
        val textViewaktu_check_out     : TextView    = vi.findViewById(R.id.txt_waktu_check_out)
        val button_check_out          : Button    =  vi.findViewById(R.id.btn_booking)
        val booking_model = arraylist_data[position]


        textViewkode_kamar.text  = booking_model.kode_kamar
        textViewnama_pemesan.text = booking_model.nama_pemesan
        textViewkode_booking.text = booking_model.kode_booking
        textViewstatus_booking.text = booking_model.status_booking
        textViewaktu_check_in.text = booking_model.waktu_check_in
        textViewaktu_check_out.text = booking_model.waktu_check_out
        Glide.with(vi.context)
            .load(booking_model.image_url)
            .into(foto_kamar)



        if (booking_model.status_booking.equals("Selesai", ignoreCase = true)) {
            // Jika status_booking adalah "Selesai", sembunyikan tombol checkout
            button_check_out.visibility = View.GONE
        }
        else {
            button_check_out.setOnClickListener() {
                val intent = Intent(context, Check_out_activity::class.java)
                intent.putExtra("nama_pemesan", booking_model.nama_pemesan)
                intent.putExtra("kode_booking", booking_model.kode_booking)
                intent.putExtra("kode_kamar", booking_model.kode_kamar)
                intent.putExtra("waktu_check_in", booking_model.waktu_check_in)
                intent.putExtra("waktu_check_out", booking_model.waktu_check_out)

                context.startActivity(intent)
            }
        }
        return vi
    }
}