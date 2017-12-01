package com.dzik.bcon.ui.main.ui.menuItems

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.dzik.bcon.R
import com.dzik.bcon.model.MenuItem
import com.dzik.bcon.model.Restaurant
import com.dzik.bcon.ui.main.MainActivity
import com.dzik.bcon.ui.main.dagger.MainActivityScope
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
) : ArrayAdapter<MenuItem>(mainActivity, 0) {

    var headerTitle = ""
    private val menuItemAddEmitter = BehaviorSubject.create<MenuItem>()

    override fun getCount(): Int {
        return super.getCount() + 1
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {

        return view ?:
                if(position == 0) {
                    val view = LayoutInflater.from(context)
                            .inflate(R.layout.menu_list_header, parent, false)

                    view.menuItemsTitle.text = headerTitle

                    view
                } else {
                    val view =  LayoutInflater.from(context)
                            .inflate(R.layout.menu_item_view, parent, false)

                    val menuItem = getItem(position - 1)

                    view.nameTextView.text = menuItem.name
                    view.priceTextView.text = menuItem.price.toString()

                    view.menuItemButton.setOnClickListener {
                        menuItemAddEmitter.onNext(menuItem)
                    }

                    picasso.load(menuItem.imageUrl)
                            .placeholder(R.drawable.ic_photo_black_48px)
                            .into(view.imageView)

                    view
        }
    }

    fun menuItemAddClicked(): Observable<MenuItem> {
        return menuItemAddEmitter.hide()
    }
}