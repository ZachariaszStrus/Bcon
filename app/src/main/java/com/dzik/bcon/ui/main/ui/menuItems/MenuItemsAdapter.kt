package com.dzik.bcon.ui.main.ui.menuItems

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dzik.bcon.R
import com.dzik.bcon.model.MenuItem
import com.dzik.bcon.ui.main.MainActivity
import com.dzik.bcon.ui.main.dagger.MainActivityScope
import com.jakewharton.rxbinding2.view.RxView
import com.squareup.picasso.Picasso
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.menu_item_view.view.*
import javax.inject.Inject




@MainActivityScope
class MenuItemsAdapter @Inject constructor (
        val mainActivity: MainActivity,
        val picasso: Picasso
) : ArrayAdapter<MenuItem>(mainActivity, 0) {

    private val addClicks: PublishSubject<MenuItem> = PublishSubject.create()

    fun addClicks() = addClicks.hide()

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        val convertView = view ?: LayoutInflater.from(context)
                .inflate(R.layout.menu_item_view, parent, false)

        val menuItem = this.getItem(position)

        convertView.nameTextView.text = menuItem.name
        convertView.priceTextView.text = menuItem.price.toString()

        picasso.load(menuItem.imageUrl)
                .into(convertView.imageView)

        return convertView
    }
}