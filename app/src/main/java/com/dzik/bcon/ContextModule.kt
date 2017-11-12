package com.dzik.bcon

import android.content.Context
import dagger.Module
import dagger.Provides


@Module
class ContextModule(
        private val context: Context
) {

    @Provides
    @BconApplicationScope
    @ApplicationContext
    fun context(): Context {
        return context
    }
}