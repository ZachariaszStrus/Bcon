package com.dzik.bcon.ui.main.ui.menuItems

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dzik.bcon.R
import com.dzik.bcon.model.MenuItem
import com.dzik.bcon.ui.main.MainActivity
import com.dzik.bcon.ui.main.dagger.MainActivityScope
import com.dzik.bcon.ui.main.viewModel.MenuItemViewModel
import com.dzik.bcon.ui.main.viewModel.MenuViewModel
import com.dzik.bcon.ui.main.viewModel.RestaurantInfoViewModel
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.menu_item_view.view.*
import kotlinx.android.synthetic.main.menu_list_header.view.*
import javax.inject.Inject


@MainActivityScope
class MenuItemsAdapter @Inject constructor (
        val mainActivity: MainActivity,
        val picasso: Picasso
) : ArrayAdapter<MenuViewModel>(mainActivity, 0) {

    private val menuItemAddEmitter = BehaviorSubject.create<MenuItem>()

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val item = getItem(position)

        return when(item) {
            is MenuItemViewModel -> {
                val convertView =  LayoutInflater.from(context)
                        .inflate(R.layout.menu_item_view, parent, false)

                convertView.nameTextView.text = item.menuItem.name
                convertView.quantityTextView.text = item.menuItem.price.toString()

                convertView.menuItemButton.setOnClickListener {
                    menuItemAddEmitter.onNext(item.menuItem)
                }

                picasso.load(item.menuItem.imageUrl)
                        .placeholder(R.drawable.ic_photo_black_48px)
                        .into(convertView.imageView)

                convertView
            }
            is RestaurantInfoViewModel -> {
                val convertView = LayoutInflater.from(context)
                        .inflate(R.layout.menu_list_header, parent, false)

                picasso.load(item.imageUrl)
                        .placeholder(R.drawable.ic_photo_black_48px)
                        .into(convertView.menuListHeaderImage)

                convertView.menuItemsTitle.text = item.name

                convertView
            }
            else -> LayoutInflater.from(context).inflate(R.layout.menu_item_view, parent, false)
        }
    }

    fun menuItemAddClicked(): Observable<MenuItem> {
        return menuItemAddEmitter.hide()
    }
}