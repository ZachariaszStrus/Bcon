package com.dzik.bcon.ui.main.mvp

import android.util.Log
import com.dzik.bcon.ui.main.dagger.MainActivityScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@MainActivityScope
class MainPresenter @Inject constructor(
        val model: MainModel,
        val mainView: MainView
) {
    private val disposables = CompositeDisposable()

    fun onCreate() {
        disposables.add(observeBeaconDetection())
    }

    fun onDestroy() {
        disposables.clear()
    }

    private fun observeBeaconDetection(): Disposable {
        return model.getRestaurantByBeacon()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    mainView.toast(it.name)
                    Log.i("Restaurant", it.toString())
                }
    }
}