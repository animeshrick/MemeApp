package com.memeapplication

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.memeapplication.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val url = "https://meme-api.com/gimme"
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
//        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        getMemeApiData()

        binding.reloadButton.setOnClickListener { getMemeApiData() }
    }

    private fun getMemeApiData() {

        val progressDialog = ProgressDialog(this).apply {
            setMessage("Wait while loading ...")
            setCancelable(false) // optional
            show() // <-- show dialog
        }

        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                // Display the first 500 characters of the response string.
//                textView.text = "Response is: ${response.substring(0, 500)}"
                Log.v("Meme Response: ",response.toString())

                var responseObject = JSONObject(response)
                binding.title.text = responseObject.getString("title")
                binding.author.text = responseObject.getString("author")

                Glide.with(this).load(responseObject.getString("url")).into(binding.imageView)
                progressDialog.dismiss()
            },
            {
                error->
                Log.e("Meme API Error:", error.localizedMessage)
                Toast.makeText(this, error.localizedMessage,Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()

            })

// Add the request to the RequestQueue.
        queue.add(stringRequest)

    }
}