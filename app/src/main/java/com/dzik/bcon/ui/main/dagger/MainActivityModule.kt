package com.dzik.bcon.ui.main.dagger

import com.dzik.bcon.ui.main.MainActivity
import dagger.Module
import dagger.Provides


@Module
class MainActivityModule(
        private val mainActivity: MainActivity
) {
    @Provides
    @MainActivityScope
    fun mainActivity(): MainActivity {
        return mainActivity
    }
}
