package com.dzik.bcon

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
import timber.log.Timber
import retrofit2.converter.gson.GsonConverterFactory







/**
 * Created by dawid on 12.08.17.
 */

@Module
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
}