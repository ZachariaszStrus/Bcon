package com.dzik.bcon.ui.main.mvp

import android.util.Log
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

    fun detectBeacons(): Observable<List<BeaconUID>> {
        return mainActivity.getBeaconDetected()
                .doOnNext { Log.i("Beacons", it.toString()) }
                .filter { it.firstOrNull() != currentBeacon }
                .doOnNext { beacons = it }
    }

    fun getRestaurantByCurrentBeacon(): Observable<Pair<Boolean,Restaurant?>> {
        return if(currentBeacon != null) {
            restaurantService.getRestaurant(currentBeacon!!.namespace,
                    currentBeacon!!.instance)
                    .doOnNext {
                        if(currentRestaurant != it) {
                            clearOrderItems()
                        }

                        currentRestaurant = it
                    }
                    .map { Pair(true, it) }
        } else {
            Observable.just(Pair(true, null))
        }
    }

    fun addOrderItem(menuItem: MenuItem): MutableMap<MenuItem, Int> {
        orderItems.put(menuItem, orderItems[menuItem]?.inc() ?: 1)
        return orderItems
    }

    fun clearOrderItems() {
        orderItems.clear()
    }

    fun removeOrderItem(menuItem: MenuItem): MutableMap<MenuItem, Int> {
        orderItems[menuItem] ?: return orderItems

        if(orderItems[menuItem] == 1) {
            orderItems.remove(menuItem)
        } else {
            orderItems.put(menuItem, orderItems[menuItem]!!.dec())
        }

        return orderItems
    }

    fun sendOrder(): Observable<Any> {
        val fcmToken = FirebaseInstanceId.getInstance().token ?: ""

        return if(currentBeacon != null) {
            Observable.just(currentBeacon)
        } else {
            detectBeacons()
        }
                .switchMap {
                    restaurantService.sendOrder(Order(orderItems, currentBeacon!!, fcmToken))
                }
    }
}