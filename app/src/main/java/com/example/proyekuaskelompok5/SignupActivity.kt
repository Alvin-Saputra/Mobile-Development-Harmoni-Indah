package com.example.proyekuaskelompok5

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class SignupActivity : AppCompatActivity() {
    private lateinit var editTextUsername       : EditText
    private lateinit var editTextPassword       : EditText
    private lateinit var editTextRePassword      : EditText
    private lateinit var btnsignup               : Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        editTextUsername = findViewById(R.id.editTextUsername)
        editTextPassword = findViewById(R.id.editTextTextPassword)
        editTextRePassword = findViewById(R.id.editTextTextRePassword)
        btnsignup = findViewById(R.id.button_signup)


        btnsignup.setOnClickListener(){


            if(editTextUsername.text.toString().isEmpty() || editTextPassword.text.toString().isEmpty() || editTextRePassword.text.toString().isEmpty())
            {
                Toast.makeText(this,"Isi Username, Password, dan RePassword", Toast.LENGTH_SHORT).show()
            }
            else {
//                val users = editTextUsername.text.toString()
                val pass = editTextPassword.text.toString()
                val repass = editTextRePassword.text.toString()

                if(pass == repass){
                    val url: String = AppConfig().IP_SERVER + "/hotel_web/signup.php"
                    val stringRequest = object : StringRequest(
                        Method.POST,url,
                        Response.Listener { response ->
                            try {
                                val jsonObj = JSONObject(response)
                                Toast.makeText(this, jsonObj.getString("error_text"), Toast.LENGTH_SHORT).show()
                                val intent = Intent(this@SignupActivity, LoginActivity::class.java)
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

                            params["username"]  = editTextUsername.text.toString()
                            params["password"]  = editTextPassword.text.toString()
//                    params["action"]    = action
                            return params
                        }
                    }
                    Volley.newRequestQueue(this).add(stringRequest)
                }
                else{
                    Toast.makeText(this,"Gagal Membuat Akun, Password dan Repassword tidak sama", Toast.LENGTH_SHORT).show()
                }
            }


        }

    }
}