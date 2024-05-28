package com.example.proyekuaskelompok5

import android.graphics.Bitmap

class Kamar_model {

    var kode_kamar: String = ""
        get() = field
        set(value) { field = value }

    var tipe_kamar: String = ""
        get() = field
        set(value) { field = value }

    var harga_per_malam: String = ""
        get() = field
        set(value) { field = value }

    var image_url: String = ""
        get() = field
        set(value) { field = value }

    var status_kamar: String = ""
        get() = field
        set(value) { field = value }

    var bitmapImage: Bitmap? = null
        get() = field
        set(value) {field = value }

}