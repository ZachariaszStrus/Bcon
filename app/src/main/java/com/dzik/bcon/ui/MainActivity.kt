package com.dzik.bcon.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.dzik.bcon.BconApplication
import com.dzik.bcon.R
import com.dzik.bcon.model.Restaurant
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val reposCall = (this.applicationContext as BconApplication)
                .component
                .restaurantService()
                .restaurantList()

        reposCall.enqueue(object : Callback<List<Restaurant>> {
            override fun onResponse(call: Call<List<Restaurant>>, response: Response<List<Restaurant>>) {
                textView.text = response.body()[0].name
            }

            override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                Log.e("dupcia", t.toString())
            }
        })
    }
}
