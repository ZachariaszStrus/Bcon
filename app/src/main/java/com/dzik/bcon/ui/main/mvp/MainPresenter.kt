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
        val view: MainView
) {
    private val disposables = CompositeDisposable()

    fun onCreate() {
        disposables.add(observeBeaconDetection())
        disposables.add(observeAddClicks())
    }

    fun onDestroy() {
        disposables.clear()
    }

    private fun observeBeaconDetection(): Disposable {
        return model.getRestaurantByBeacon()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnEach { view.showProgress(false) }
                .subscribe {
                    view.updateRestaurant(it)
                    Log.i("Restaurant", it.toString())
                }
    }

    private fun observeAddClicks(): Disposable {
        return view.addClicks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe {
                    view.updateOrderItems(model.addOrderItem(it))
                }
    }
}