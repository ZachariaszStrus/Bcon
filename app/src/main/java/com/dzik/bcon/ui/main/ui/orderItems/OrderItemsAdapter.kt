package com.dzik.bcon.ui.main.ui.orderItems

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dzik.bcon.R
import com.dzik.bcon.model.MenuItem
import com.dzik.bcon.ui.main.MainActivity
import com.dzik.bcon.ui.main.dagger.MainActivityScope
import com.dzik.bcon.ui.main.viewModel.OrderItemViewModel
import com.squareup.picasso.Picasso
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.order_item_view.view.*
import javax.inject.Inject


@MainActivityScope
class OrderItemsAdapter @Inject constructor (
        val mainActivity: MainActivity,
        val picasso: Picasso
) : ArrayAdapter<OrderItemViewModel>(mainActivity, 0) {

    val itemRemoveEmitter = BehaviorSubject.create<MenuItem>()

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {

        val convertView = view ?: LayoutInflater.from(context)
                .inflate(R.layout.order_item_view, parent, false)

        val orderItem = this.getItem(position)

        convertView.removeButton.setOnClickListener {
            itemRemoveEmitter.onNext(orderItem.menuItem)
        }

        convertView.nameTextView.text = orderItem.menuItem.name
        convertView.quantityTextView.text = "x ${orderItem.quantity}"

        picasso.load(orderItem.menuItem.imageUrl)
                .placeholder(R.drawable.ic_photo_black_48px)
                .into(convertView.imageView)

        return convertView
    }
}