package com.dzik.bcon.ui.main.mvp

import android.util.Log
import com.dzik.bcon.model.BeaconUID
import com.dzik.bcon.model.MenuItem
import com.dzik.bcon.model.Restaurant
import com.dzik.bcon.service.RestaurantService
import com.dzik.bcon.ui.main.MainActivity
import com.dzik.bcon.ui.main.dagger.MainActivityScope
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject


@MainActivityScope
class MainModel @Inject constructor(
        val mainActivity: MainActivity,
        val restaurantService: RestaurantService
) {
    val orderItems = mutableListOf<MenuItem>()

    fun detectBeacon(): Observable<BeaconUID> {
        return mainActivity.getBeaconDetected()
    }

    fun getRestaurantByBeacon(beaconUID: BeaconUID): Observable<Restaurant> {
        return restaurantService.getRestaurant(beaconUID.namespace, beaconUID.instance)
    }

    fun addOrderItem(menuItem: MenuItem): List<MenuItem> {
        orderItems.add(menuItem)
        return orderItems
    }
}