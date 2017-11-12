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

        return model.detectBeacon()
                .distinctUntilChanged()
                .doOnNext { Log.i("MAIN_PRESENTER new beacon:", it.toString()) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { view.showProgress(true) }
                .observeOn(Schedulers.io())
                .switchMap { model.getRestaurantByBeacon(it) }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnEach { view.showProgress(false) }
                .doOnNext { view.updateRestaurant(it) }
                .doOnError { Log.e("MAIN_PRESENTER", it.message) }
                .subscribe()
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