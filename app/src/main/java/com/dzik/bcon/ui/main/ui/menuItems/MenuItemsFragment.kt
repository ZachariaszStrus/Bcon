package com.dzik.bcon.ui.main.ui.menuItems

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dzik.bcon.R
import com.dzik.bcon.model.Restaurant
import com.dzik.bcon.ui.main.dagger.MainActivityScope
import kotlinx.android.synthetic.main.menu_items_fragment.*
import javax.inject.Inject


@SuppressLint("ValidFragment")
@MainActivityScope
class MenuItemsFragment @Inject constructor(
        val menuItemsAdapter: MenuItemsAdapter
) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.menu_items_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity.menu_list_view.adapter = this.menuItemsAdapter
    }

    fun updateRestaurant(restaurant: Restaurant) {
        menuItemsAdapter.clear()
        menuItemsAdapter.addAll(restaurant.menu)
    }

    fun showProgress(show: Boolean) {
        if(show) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
        }
    }

    fun addClicks() = menuItemsAdapter.addClicks()
}