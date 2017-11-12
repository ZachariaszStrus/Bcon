package com.dzik.bcon

import android.content.Context
import com.dzik.bcon.service.RestaurantService
import dagger.Module
import retrofit2.Retrofit
import dagger.Provides
import com.fatboyindustrial.gsonjodatime.DateTimeConverter
import org.joda.time.DateTime
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.service.ArmaRssiFilter
import org.altbeacon.beacon.service.RunningAverageRssiFilter
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import timber.log.Timber
import retrofit2.converter.gson.GsonConverterFactory


@Module(includes = arrayOf(ContextModule::class))
class BconApplicationModule {

    @Provides
    @BconApplicationScope
    fun restaurantService(bconRetrofit: Retrofit): RestaurantService {
        return bconRetrofit.create(RestaurantService::class.java)
    }


    @Provides
    @BconApplicationScope
    fun gson(): Gson {
        val gsonBuilder = GsonBuilder()
        gsonBuilder.registerTypeAdapter(DateTime::class.java, DateTimeConverter())
        return gsonBuilder.create()
    }

    @Provides
    @BconApplicationScope
    fun retrofit(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl("https://bcon-spring.herokuapp.com/")
                .build()
    }

    @Provides
    @BconApplicationScope
    fun loggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Timber.i(message) })
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        return interceptor
    }

    @Provides
    @BconApplicationScope
    fun okHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
    }

    @Provides
    @BconApplicationScope
    fun beaconManager(@ApplicationContext context: Context): BeaconManager {
        BeaconManager.setRssiFilterImplClass(ArmaRssiFilter::class.java)

        val manager = BeaconManager.getInstanceForApplication(context)
        manager.beaconParsers.add(BeaconParser().setBeaconLayout(
                BeaconParser.EDDYSTONE_UID_LAYOUT))
        return manager
    }
}