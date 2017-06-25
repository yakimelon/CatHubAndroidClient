package com.example.allen.cathubclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.io.InputStream
import java.net.URL
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.0.10:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        val catCall = retrofit
                .create<CatHubApi>(CatHubApi::class.java)
                .getImages()

        catCall.enqueue(object : Callback<CatImage> {
            override fun onResponse(call: Call<CatImage>, response: Response<CatImage>) {
                val cat = response.body()
                Log.d("成功", cat?.result)
                Thread( { setImage(cat?.items!!.first().filename,10) } ).start()
            }

            override fun onFailure(call: Call<CatImage>, t: Throwable) {
                Log.d("失敗", t.message)
            }
        })
    }

    private fun setImage(fileUrl: String?, addY: Int) {
        val istream: InputStream = URL("http://192.168.0.10:3000" + fileUrl).openStream()
        val bitmapImage: Bitmap = BitmapFactory.decodeStream(istream)
        istream.close()

        // UI操作スレッドで画像追加
        runOnUiThread({
            val imageView: ImageView = ImageView(applicationContext)
            imageView.setImageBitmap(bitmapImage)
            imageBox.addView(imageView)
        })
    }
}
