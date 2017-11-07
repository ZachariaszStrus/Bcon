package com.dzik.bcon.ui.main.mvp

import android.util.Log
import com.dzik.bcon.model.Restaurant
import com.dzik.bcon.service.RestaurantService
import com.dzik.bcon.ui.main.MainActivity
import com.dzik.bcon.ui.main.dagger.MainActivityScope
import io.reactivex.Observable
import javax.inject.Inject


@MainActivityScope
class MainModel @Inject constructor(
        val mainActivity: MainActivity,
        val restaurantService: RestaurantService
) {
    fun getRestaurantByBeacon(): Observable<Restaurant> {
        return mainActivity.beaconDetected
                .distinctUntilChanged()
                .switchMap {
                    Log.i("New beacon", it.toString())
                    restaurantService.getRestaurant(it.namespace, it.instance)
                }
    }

//    fun getRestaurantByBeacon(): Observable<Restaurant> {
//        return restaurantService
//                .getRestaurant("edd1ebeac04e5defa017", "89fac117b149")
//    }
}