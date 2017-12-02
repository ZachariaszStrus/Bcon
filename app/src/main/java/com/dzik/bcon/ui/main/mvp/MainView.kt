package com.dzik.bcon.ui.main.mvp

import android.annotation.SuppressLint
import android.support.constraint.ConstraintLayout
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.dzik.bcon.R
import com.dzik.bcon.model.MenuItem
import com.dzik.bcon.model.Restaurant
import com.dzik.bcon.ui.main.MainActivity
import com.dzik.bcon.ui.main.dagger.MainActivityScope
import com.dzik.bcon.ui.main.ui.BeaconNotFoundFragment
import com.dzik.bcon.ui.main.ui.MainFragmentPagerAdapter
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
        val orderItemsFragment: OrderItemsFragment,
        val beaconNotFoundFragment: BeaconNotFoundFragment
) : ConstraintLayout(mainActivity) {

    private val visitedTabs = mutableListOf<Fragment>()

    private var isBeaconFound: Boolean = false
        set(value) {
            if(value)
                mainViewPager.currentItem = 1
            else
                mainViewPager.currentItem = 0

            field = value
        }

    init {
        View.inflate(mainActivity, R.layout.activity_main, this)

        val fragmentPager = MainFragmentPagerAdapter(mainActivity.supportFragmentManager,
                mutableListOf(beaconNotFoundFragment, menuItemsFragment, orderItemsFragment))
        mainViewPager.adapter = fragmentPager
        mainViewPager.offscreenPageLimit = 4

        navigation.setOnNavigationItemSelectedListener(
                BottomNavigationView.OnNavigationItemSelectedListener { item ->

                    val dupa = mainViewPager

                    when (item.itemId) {
                        R.id.menu_items_tab -> {
                            if(isBeaconFound)
                                mainViewPager.currentItem = 1
                            else
                                mainViewPager.currentItem = 0

                            return@OnNavigationItemSelectedListener true
                        }
                        R.id.order_items_tab -> {
                            mainViewPager.currentItem = 2
                            return@OnNavigationItemSelectedListener true
                        }
                    }
                    false
                })

        //this.show(frame_layout, beaconNotFoundFragment)

        menuItemAddClicked().subscribe { menuItem ->
            updateOrderItems(listOf(menuItem, menuItem, menuItem))
        }
    }

    /*private fun show(dsc: FrameLayout, src: Fragment) {
        val fragmentTransaction = mainActivity.supportFragmentManager.beginTransaction()
        this.visitedTabs.forEach { fragment -> fragmentTransaction.hide(fragment) }

        if (!this.visitedTabs.contains(src)) {
            fragmentTransaction.add(dsc.id, src)
            this.visitedTabs.add(src)
        } else fragmentTransaction.show(src)

        fragmentTransaction.commit()
    }*/

    fun updateRestaurant(restaurant: Restaurant?) {
        if(restaurant != null) {
            isBeaconFound = true
            menuItemsFragment.updateRestaurant(restaurant)
        } else
            isBeaconFound = false

    }

    fun showProgress(show: Boolean) {
        beaconNotFoundFragment.showProgress(show)
    }

    fun menuItemsRefreshed(): Observable<Unit> {
        return menuItemsFragment.menuItemsRefreshed()
    }

    fun menuItemsSetRefreshing(value: Boolean) {
        menuItemsFragment.menuItemsSetRefreshing(value)
    }

    fun updateOrderItems(newList: List<MenuItem>) {
        orderItemsFragment.updateList(newList)
    }

    fun menuItemAddClicked(): Observable<MenuItem> {
        return menuItemsFragment.menuItemAddClicked()
    }
}