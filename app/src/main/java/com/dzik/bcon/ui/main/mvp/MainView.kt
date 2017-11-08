package com.dzik.bcon.ui.main.mvp

import android.annotation.SuppressLint
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.View
import android.widget.FrameLayout
import com.dzik.bcon.R
import com.dzik.bcon.model.MenuItem
import com.dzik.bcon.model.Restaurant
import com.dzik.bcon.ui.main.MainActivity
import com.dzik.bcon.ui.main.dagger.MainActivityScope
import com.dzik.bcon.ui.main.ui.menuItems.MenuItemsFragment
import com.dzik.bcon.ui.main.ui.orderItems.OrderItemsFragment
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.menu_items_fragment.*
import javax.inject.Inject


@SuppressLint("ViewConstructor")
@MainActivityScope
class MainView @Inject constructor(
        val mainActivity: MainActivity,
        val menuItemsFragment: MenuItemsFragment,
        val orderItemsFragment: OrderItemsFragment
) : ConstraintLayout(mainActivity) {

    private val visitedTabs = mutableListOf<Fragment>()

    init {
        View.inflate(mainActivity, R.layout.activity_main, this)

        navigation.setOnNavigationItemSelectedListener(
                BottomNavigationView.OnNavigationItemSelectedListener { item ->
                    when (item.itemId) {
                        R.id.menu_items_tab -> {
                            this.show(frame_layout, menuItemsFragment)
                            return@OnNavigationItemSelectedListener true
                        }
                        R.id.order_items_tab -> {
                            this.show(frame_layout, orderItemsFragment)
                            return@OnNavigationItemSelectedListener true
                        }
                    }
                    false
                })

        this.show(frame_layout, menuItemsFragment)
    }

    private fun show(dsc: FrameLayout, src: Fragment) {
        val fragmentTransaction = mainActivity.supportFragmentManager.beginTransaction()
        this.visitedTabs.forEach { fragment -> fragmentTransaction.hide(fragment) }

        if (!this.visitedTabs.contains(src)) {
            fragmentTransaction.add(dsc.id, src)
            this.visitedTabs.add(src)
        } else fragmentTransaction.show(src)

        fragmentTransaction.commit()
    }

    fun updateRestaurant(restaurant: Restaurant) {
        menuItemsFragment.updateRestaurant(restaurant)
    }

    fun showProgress(show: Boolean) {
        menuItemsFragment.showProgress(show)
    }

    fun addClicks(): Observable<MenuItem> {
        return menuItemsFragment.addClicks()
    }

    fun updateOrderItems(newList: List<MenuItem>) {
        orderItemsFragment.updateList(newList)
    }
}