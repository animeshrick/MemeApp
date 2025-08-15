package com.memeapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.memeapplication.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val url = "https://meme-api.com/gimme"
    val dummyImage =
        "https://tse2.mm.bing.net/th/id/OIP.-a13rG_rPZnTuMfn5pj30wHaHa?r=0&rs=1&pid=ImgDetMain&o=7&rm=3"
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

//        val progressDialog = ProgressDialog(this).apply {
//            setMessage("Wait while loading ...")
//            setCancelable(false) // optional
//            show() // <-- show dialog
//        }

        binding.progressBar.visibility = View.VISIBLE
        binding.progressBar.isEnabled = false

        val queue = Volley.newRequestQueue(this)

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                Log.v("Meme Response: ", response.toString())

                val responseObject = JSONObject(response)

                binding.title.text = responseObject.optString("title")
                    .takeIf { it.isNotBlank() } ?: "Dummy Title"
                binding.author.text = responseObject.optString("author")
                    .takeIf { it.isNotBlank() } ?: "Dummy Author"

                Glide.with(this)
                    .load(responseObject.optString("url").takeIf { it.isNotBlank() } ?: dummyImage)
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(binding.imageView)


                binding.progressBar.visibility = View.GONE
                binding.progressBar.isEnabled = true

//                progressDialog.dismiss()
            },
            { error ->
                error.localizedMessage?.let { Log.e("Meme API Error: ", it) }
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_SHORT).show()
//                progressDialog.dismiss()
            })

        queue.add(stringRequest)

    }
}