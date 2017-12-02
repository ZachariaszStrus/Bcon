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
    val currentBeacon
        get() = beacons.firstOrNull()

    var orderItems = mutableMapOf<MenuItem, Int>()

    var beacons: List<BeaconUID> = emptyList()

    var currentRestaurant: Restaurant? = null

    fun detectBeacon(): Observable<BeaconUID?> {
        return mainActivity.getBeaconDetected()
                .doOnNext { beacons = it }
                .map { currentBeacon }
    }

    fun getRestaurantByBeacon(beaconUID: BeaconUID?): Observable<Restaurant?> {
        return if(beaconUID != null) {
            restaurantService.getRestaurant(beaconUID.namespace, beaconUID.instance)
                    .doOnNext { currentRestaurant = it }
        } else {
            Observable.just(null)
        }
    }

    fun addOrderItem(menuItem: MenuItem): MutableMap<MenuItem, Int> {
        orderItems.put(menuItem, orderItems[menuItem]?.inc() ?: 1)
        return orderItems
    }

    fun clearOrderItems() {
        orderItems.clear()
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