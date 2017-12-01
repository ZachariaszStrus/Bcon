package com.dzik.bcon.service

import com.dzik.bcon.model.Order
import com.dzik.bcon.model.Restaurant
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface RestaurantService {

    @GET("restaurants")
    fun getRestaurant(
            @Query("beaconNamespace") beaconNamespace: String,
            @Query("beaconInstance") beaconInstance: String
    ): Observable<Restaurant>

    @POST("orders")
    fun sendOrder(
            @Body order: Order
    ): Observable<Any>

}