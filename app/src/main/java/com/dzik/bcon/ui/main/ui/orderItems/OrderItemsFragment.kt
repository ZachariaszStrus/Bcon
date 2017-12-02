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
import kotlinx.android.synthetic.main.menu_items_fragment.*
import kotlinx.android.synthetic.main.order_items_fragment.*
import javax.inject.Inject


@SuppressLint("ValidFragment")
@MainActivityScope
class OrderItemsFragment @Inject constructor(
        val orderItemsAdapter: OrderItemsAdapter
): Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.order_items_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ordersListView.adapter = this.orderItemsAdapter
    }

    fun updateList(newList: List<MenuItem>) {
        orderItemsAdapter.clear()
        orderItemsAdapter.addAll(newList)
    }
}
