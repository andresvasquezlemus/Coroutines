package com.crisspian.martes_25_08_coroutines

import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class MainActivity : AppCompatActivity() {

    val urls = listOf<String>("https://apod.nasa.gov/apod/image/1908/M61-HST-ESO-S1024.jpg",
        "https://hips.hearstapps.com/hmg-prod.s3.amazonaws.com/images/andromeda-galaxy-royalty-free-image-1585682435.jpg",
        "https://media.wired.com/photos/5a593a7ff11e325008172bc2/125:94/w_2393,h_1800,c_limit/pulsar-831502910.jpg",
        "https://scitechdaily.com/images/Illustration-Photons-Galaxy.jpg")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var progressBarList = listOf<ProgressBar>(progressBar_image01, progressBar_image02, progressBar_image03, progressBar_image04)

        var imageViewList = listOf<ImageView>(imageView_image01, imageView_image02, imageView_image03, imageView_image04)

        button_Message.setOnClickListener {
            AlertDialog
                .Builder(this)
                .setMessage("Funcionamiento de las Corrutinas en background")
                .setCancelable(true)
                .show()
        }

        CoroutineScope(Dispatchers.Main).launch {
            for(i in urls.indices) {
                val image = doInBackground(urls[i], progressBarList[i])
                Log.d("COROUTINE", image.toString())
                if (image != null) {
                    updateView(image, progressBarList[i], imageViewList[i])
                }
            }
        }

    }



    private suspend fun doInBackground(url: String, progressBar: ProgressBar): Bitmap? {
        var bmp: Bitmap? = null
        withContext(Dispatchers.Default) {
            try {
                progressBar.visibility = View.VISIBLE
                val newURL = URL(url)
                val inputStream = newURL.openConnection().getInputStream()
                Log.d("INPUT", inputStream.toString())
                bmp = BitmapFactory.decodeStream(inputStream)

            } catch (e: Exception) {
                Log.e("ERROR", e.message.toString())
                e.printStackTrace()
            }
        }
        return bmp
    }

    private fun updateView(result: Bitmap, progressBar: ProgressBar, imageView: ImageView) {
        Log.d("BAR", "entró al updateView()")
        progressBar.visibility = View.GONE
        imageView.setImageBitmap(result)
    }


}