package com.dzik.bcon

import com.dzik.bcon.service.RestaurantService
import dagger.Component
import org.altbeacon.beacon.BeaconManager

@BconApplicationScope
@Component(modules = arrayOf(BconApplicationModule::class))
interface BconApplicationComponent {

    fun restaurantService(): RestaurantService

    fun beaconManager(): BeaconManager

}