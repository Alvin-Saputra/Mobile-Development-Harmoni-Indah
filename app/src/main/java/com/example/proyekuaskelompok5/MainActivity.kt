package com.example.proyekuaskelompok5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.imageview.ShapeableImageView

class MainActivity : AppCompatActivity() {
    private lateinit var menu_add_kamar       : Button
    private lateinit var menu_list_kamar    : Button
    private lateinit var menu_list_booking  : Button
    private lateinit var add_data_kamar_head : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        menu_add_kamar  = findViewById(R.id.button_add_kamar)
        menu_list_kamar  = findViewById(R.id.button_list_kamar)
        menu_list_booking = findViewById(R.id.button_list_booking)
        add_data_kamar_head = findViewById(R.id.button_head_add_data_kamar)

        menu_add_kamar.setOnClickListener(){
            val intent = Intent(this@MainActivity, CRUD_Kamar_Activity::class.java)
            startActivity(intent)
        }

        add_data_kamar_head.setOnClickListener(){
            val intent = Intent(this@MainActivity, CRUD_Kamar_Activity::class.java)
            startActivity(intent)
        }

        menu_list_kamar.setOnClickListener(){
            val intent = Intent(this@MainActivity, View_Data_CRUD_Kamar_Activity::class.java)
            startActivity(intent)
        }

        menu_list_booking.setOnClickListener(){
            val intent = Intent(this@MainActivity, View_Data_Booking_Activity::class.java)
            startActivity(intent)
        }

    }
}