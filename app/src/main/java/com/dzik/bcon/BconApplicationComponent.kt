package com.dzik.bcon

import android.content.Context
import com.dzik.bcon.service.RestaurantService
import com.squareup.picasso.Picasso
import dagger.Component
import org.altbeacon.beacon.BeaconManager

@BconApplicationScope
@Component(modules = [(BconApplicationModule::class), (PicassoModule::class)])
interface BconApplicationComponent {

    fun restaurantService(): RestaurantService

    fun beaconManager(): BeaconManager

    fun picasso(): Picasso
}