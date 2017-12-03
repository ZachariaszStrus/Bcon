package com.dzik.bcon.ui.main.mvp

import android.util.Log
import com.dzik.bcon.model.BeaconUID
import com.dzik.bcon.ui.main.dagger.MainActivityScope
import com.dzik.bcon.ui.main.viewModel.OrderItemViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
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
        disposables.add(observeRefreshSwipe())
        disposables.add(observeAddClicks())
        disposables.add(observeSendClicks())
        disposables.add(observeClearClicks())
    }

    fun onDestroy() {
        disposables.clear()
    }

    private fun observeBeaconDetection() = model.detectBeacon()
            .doOnNext { Log.i("MAIN_PRESENTER new beacon", it?.instance) }
            .distinctUntilChanged()
            .downloadRestaurantData()
            .doOnNext { Log.i("MAIN_PRESENTER restaurant", it?.name) }
            .doOnNext { model.clearOrderItems() }
            .doOnNext { view.updateRestaurant(it) }
            .doOnError { Log.e("MAIN_PRESENTER", it.message) }
            .subscribe()


    private fun observeRefreshSwipe() = view.menuItemsRefreshed()
            .map { model.currentBeacon }
            .downloadRestaurantData()
            .doOnNext { view.updateRestaurant(it) }
            .doOnEach { view.menuItemsSetRefreshing(false) }
            .subscribe()


    private fun Observable<BeaconUID?>.downloadRestaurantData() = this
            .observeOn(AndroidSchedulers.mainThread())
            .observeOn(Schedulers.io())
            .switchMap { model.getRestaurantByBeacon(model.currentBeacon) }
            .observeOn(AndroidSchedulers.mainThread())


    private fun observeAddClicks() = view.menuItemAddClicked()
            .subscribe {
                model.addOrderItem(it)
                updateOrderList()
            }

    private fun observeSendClicks() = view.orderListView.sendClick()
            .observeOn(Schedulers.io())
            .switchMap { model.sendOrder() }

            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                model.clearOrderItems()
                updateOrderList()
            }
            .subscribe()

    private fun observeClearClicks() = view.orderListView.clearClick()
            .doOnNext {
                model.clearOrderItems()
                updateOrderList()
            }
            .subscribe()


    private fun updateOrderList() {
        view.orderListView.updateList(model.orderItems.map {
            OrderItemViewModel(
                    it.key,
                    it.value
            )
        })
    }
}