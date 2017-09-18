package com.dzik.bcon

import com.dzik.bcon.service.RestaurantService
import dagger.Component

/**
 * Created by dawid on 12.08.17.
 */

@BconApplicationScope
@Component(modules = arrayOf(BconApplicationModule::class))
interface BconApplicationComponent {
    fun restaurantService(): RestaurantService
}