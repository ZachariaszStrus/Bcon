package com.dzik.bcon.ui.main.ui.menuItems

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dzik.bcon.R
import com.dzik.bcon.model.MenuItem
import com.dzik.bcon.model.Restaurant
import com.dzik.bcon.ui.main.dagger.MainActivityScope
import com.squareup.picasso.Picasso
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.menu_items_fragment.*
import javax.inject.Inject


@SuppressLint("ValidFragment")
@MainActivityScope
class MenuItemsFragment @Inject constructor(
        val menuItemsAdapter: MenuItemsAdapter,
        val picasso: Picasso
) : Fragment() {

    private val restaurantEmitter = BehaviorSubject.create<Restaurant>()
    private val restaurantRefreshEmitter = BehaviorSubject.create<Unit>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.menu_items_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        menu_list_view.adapter = this.menuItemsAdapter

        restaurantEmitter.subscribe { restaurant ->
            menuItemsAdapter.clear()
            menuItemsAdapter.headerTitle = restaurant.name
            menuItemsAdapter.addAll(restaurant.menu)

            /*picasso
                    .load(restaurant.imageUrl)
                    .into(restaurantImageView)*/


            //nameTextView.text = restaurant.name
        }

        menuListRefresh.setOnRefreshListener {
            restaurantRefreshEmitter.onNext(Unit)
        }
    }

    fun menuItemAddClicked(): Observable<MenuItem> {
        return menuItemsAdapter.menuItemAddClicked()
    }

    fun menuItemsRefreshed(): Observable<Unit> {
        return restaurantRefreshEmitter.hide()
    }

    fun menuItemsSetRefreshing(value: Boolean) {
        menuListRefresh.isRefreshing = value
    }

    fun updateRestaurant(restaurant: Restaurant) {
        restaurantEmitter.onNext(restaurant)
    }
}