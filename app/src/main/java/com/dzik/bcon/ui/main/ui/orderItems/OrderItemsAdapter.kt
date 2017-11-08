//package com.dzik.bcon.ui.main.ui.orderItems
//
//import android.content.Context
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import com.dzik.bcon.R
//import com.dzik.bcon.model.MenuItem
//import com.dzik.bcon.ui.main.dagger.MainActivityScope
//import kotlinx.android.synthetic.main.order_item_view.view.*
//import javax.inject.Inject
//
//
//@MainActivityScope
//class OrderItemsAdapter @Inject constructor (
//        appContext: Context
//) : ArrayAdapter<MenuItem>(appContext, 0) {
//
//    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
//
//        val convertView = view ?: LayoutInflater.from(context)
//                .inflate(R.layout.order_items_fragment, parent, false)
//
//        val menuItem = this.getItem(position)
//
//        convertView.nameTextView.text = menuItem.name
//        convertView.priceTextView.text = menuItem.price.toString()
//        convertView.quantityTextView.text = "1"
//
//        return convertView
//    }
//}