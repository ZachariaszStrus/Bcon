package com.dzik.bcon.service

import com.dzik.bcon.model.Restaurant
import retrofit2.Call
import retrofit2.http.GET

/**
 * Created by dawid on 12.08.17.
 */

interface RestaurantService {
    @GET("restaurants")
    fun restaurantList(): Call<List<Restaurant>>

}