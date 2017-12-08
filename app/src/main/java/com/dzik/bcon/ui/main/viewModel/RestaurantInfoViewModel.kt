package com.dzik.bcon.ui.main.viewModel

import com.dzik.bcon.model.PaymentOption


class RestaurantInfoViewModel (

        val name: String,

        val imageUrl: String,

        val paymentOptions: MutableList<PaymentOption>

) : MenuViewModel