package com.dzik.bcon.ui.main.ui.orderItems

import com.dzik.bcon.model.MenuItem
import com.dzik.bcon.ui.main.viewModel.OrderItemViewModel
import io.reactivex.Observable


interface OrderListView {

    fun updateList(newList: List<OrderItemViewModel>)

    fun orderItemRemoveClick(): Observable<MenuItem>

    fun sendClick(): Observable<Unit>

    fun clearClick(): Observable<Unit>

    fun setProgress(show: Boolean)
}