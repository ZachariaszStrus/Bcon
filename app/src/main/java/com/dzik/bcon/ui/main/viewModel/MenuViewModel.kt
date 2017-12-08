package com.dzik.bcon.ui.main.viewModel

import com.dzik.bcon.model.MenuItem
import com.dzik.bcon.model.PaymentOption


sealed class MenuViewModel

data class MenuItemViewModel (

        val menuItem: MenuItem

) : MenuViewModel()

class RestaurantInfoViewModel (

        val name: String,

        val imageUrl: String,

        val paymentOptions: MutableList<PaymentOption>

) : MenuViewModel()