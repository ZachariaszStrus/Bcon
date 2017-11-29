package com.dzik.bcon.ui.main.ui

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dzik.bcon.R
import com.dzik.bcon.ui.main.dagger.MainActivityScope
import javax.inject.Inject

@SuppressLint("ValidFragment")
@MainActivityScope
class BeaconNotFoundFragment @Inject constructor(): Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.beacon_not_found_fragment_view, container, false)
    }
}