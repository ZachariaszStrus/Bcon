package com.dzik.bcon

import android.app.Application

/**
 * Created by dawid on 12.08.17.
 */
class BconApplication : Application() {
    lateinit var component: BconApplicationComponent

    override fun onCreate() {
        super.onCreate()

        component = DaggerBconApplicationComponent.builder()
                .build()

    }
}