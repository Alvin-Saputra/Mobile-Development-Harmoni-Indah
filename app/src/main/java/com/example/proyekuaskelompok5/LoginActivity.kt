package com.example.proyekuaskelompok5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
//    private lateinit var action             : String
    private lateinit var editTextNama       : EditText
    private lateinit var editTextPassword   : EditText
    private lateinit var btnlogin           : Button
    private lateinit var signuplink         : TextView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        editTextNama = findViewById(R.id.editTextNama)
        editTextPassword = findViewById(R.id.editTextTextPassword)
        btnlogin = findViewById(R.id.button_add_kamar)
        signuplink = findViewById(R.id.textView_dont_have_account)

        btnlogin.setOnClickListener(){
            val url: String = AppConfig().IP_SERVER + "/hotel_web/login.php"
            val stringRequest = object : StringRequest(Method.POST,url,
                Response.Listener { response ->
                    Log.d("res", response)
                    val jsonObj = JSONObject(response)
                    Toast.makeText(this,jsonObj.getString("error_text"),Toast.LENGTH_SHORT).show()
                    if (jsonObj.getString("error_text") == "Success Login") {
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else if (jsonObj.getString("error_text") == "Login Failed") {
                        Toast.makeText(this, "Login gagal, cek kembali username dan password", Toast.LENGTH_SHORT).show()
                    }
                },
                Response.ErrorListener { _ ->
                    Toast.makeText(this,"Gagal Terhubung",Toast.LENGTH_SHORT).show()
                }
            ){
                override fun getParams(): HashMap<String,String>{
                    val params = HashMap<String,String>()

                    params["nama"]      = editTextNama.text.toString()
                    params["password"]  = editTextPassword.text.toString()
//                    params["action"]    = action
                    return params
                }
            }
            Volley.newRequestQueue(this).add(stringRequest)
        }

        signuplink.setOnClickListener(){
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }


    }
}