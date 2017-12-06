package com.dzik.bcon.ui.main.ui.orderItems

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dzik.bcon.R
import com.dzik.bcon.model.MenuItem
import com.dzik.bcon.ui.main.dagger.MainActivityScope
import com.dzik.bcon.ui.main.viewModel.OrderItemViewModel
import com.jakewharton.rxbinding2.view.RxView
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.order_item_view.*
import kotlinx.android.synthetic.main.order_items_fragment.*
import javax.inject.Inject


@SuppressLint("ValidFragment")
@MainActivityScope
class OrderItemsFragment @Inject constructor(
        val orderItemsAdapter: OrderItemsAdapter
): Fragment(), OrderListView {

    private val sendEmitter = BehaviorSubject.create<Unit>()

    private val clearEmitter = BehaviorSubject.create<Unit>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.order_items_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ordersListView.adapter = this.orderItemsAdapter

        sendButton.setOnClickListener { sendEmitter.onNext(Unit) }

        clearButton.setOnClickListener { clearEmitter.onNext(Unit) }
    }

    override fun updateList(newList: List<OrderItemViewModel>) {
        orderItemsAdapter.clear()
        orderItemsAdapter.addAll(newList)

        if(newList.isEmpty()) {
            sendButton.visibility = View.GONE
            clearButton.visibility = View.GONE
        } else {
            sendButton.visibility = View.VISIBLE
            clearButton.visibility = View.VISIBLE
        }
    }

    override fun orderItemRemoveClick() = orderItemsAdapter.itemRemoveEmitter.hide()

    override fun sendClick() = sendEmitter.hide()

    override fun clearClick() = clearEmitter.hide()

    override fun setProgress(show: Boolean) {
        progressBar.visibility = if(show) View.VISIBLE else View.GONE
    }
}
