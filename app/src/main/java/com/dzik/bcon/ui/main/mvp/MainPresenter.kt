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
        disposables.addAll(
                observeBeaconDetection(),
                observeRefreshSwipe(),
                observeAddClicks(),
                observeSendClicks(),
                observeClearClicks()
        )
    }

    fun onDestroy() {
        disposables.clear()
    }

    private fun observeBeaconDetection() = model.detectBeacons()
            .downloadRestaurantData()
            .subscribe()


    private fun observeRefreshSwipe() = view.menuItemsRefreshed()
            .downloadRestaurantData()
            .doOnEach { view.menuItemsSetRefreshing(false) }
            .subscribe()


    private fun <T> Observable<T>.downloadRestaurantData() = this
            .observeOn(Schedulers.io())
            .switchMap { model.getRestaurantByCurrentBeacon() }
            .observeOn(AndroidSchedulers.mainThread())
            .doOnEach {
                model.clearOrderItems()
            }
            .doOnNext {
                Log.i("MAIN_PRESENTER restaurant", it?.second?.name ?: "null")
                view.updateRestaurant(it?.second)
            }


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