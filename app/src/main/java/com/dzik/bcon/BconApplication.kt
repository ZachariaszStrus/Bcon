package com.dzik.bcon

import android.app.Application
import android.content.DialogInterface
import android.content.pm.PackageManager




class BconApplication : Application() {
    companion object {
        lateinit var component: BconApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()

        component = DaggerBconApplicationComponent.builder()
                .bconApplicationModule(BconApplicationModule(this))
                .build()

    }
}