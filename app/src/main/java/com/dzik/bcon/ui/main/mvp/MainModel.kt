package com.dzik.bcon.ui.main.mvp

import com.dzik.bcon.model.BeaconUID
import com.dzik.bcon.model.MenuItem
import com.dzik.bcon.model.Order
import com.dzik.bcon.model.Restaurant
import com.dzik.bcon.service.RestaurantService
import com.dzik.bcon.ui.main.MainActivity
import com.dzik.bcon.ui.main.dagger.MainActivityScope
import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Observable
import javax.inject.Inject


@MainActivityScope
class MainModel @Inject constructor(
        val mainActivity: MainActivity,
        val restaurantService: RestaurantService
) {
    var orderItems = mutableListOf<MenuItem>()

    var currentBeacon: BeaconUID? = null

    var currentRestaurant: Restaurant? = null

    fun detectBeacon(): Observable<BeaconUID> {
        return mainActivity.getBeaconDetected()
                .doOnNext { currentBeacon = it }
    }

    fun getRestaurantByBeacon(beaconUID: BeaconUID): Observable<Restaurant> {
        return restaurantService.getRestaurant(beaconUID.namespace, beaconUID.instance)
                .doOnNext { currentRestaurant = it }
    }

    fun addOrderItem(menuItem: MenuItem): List<MenuItem> {
        orderItems.add(menuItem)
        return orderItems
    }

    fun sendOrder(): Observable<Any> {
        val fcmToken = FirebaseInstanceId.getInstance().token ?: ""

        return if(currentBeacon != null) {
            Observable.just(currentBeacon)
        } else {
            detectBeacon()
        }
                .switchMap {
                    restaurantService.sendOrder(Order(orderItems, currentBeacon!!, fcmToken))
                }
    }
}