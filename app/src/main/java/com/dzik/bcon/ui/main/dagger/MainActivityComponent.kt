package com.dzik.bcon.ui.main.dagger

import com.dzik.bcon.BconApplicationComponent
import com.dzik.bcon.ui.main.MainActivity
import dagger.Component


@MainActivityScope
@Component(modules = arrayOf(MainActivityModule::class),
        dependencies = arrayOf(BconApplicationComponent::class))
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)
}
