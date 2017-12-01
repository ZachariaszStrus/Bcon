package com.dzik.bcon.model


data class OrderItem (

        val menuItemId: Int,

        val quantity: Int
) {
        constructor(menuItem: MenuItem) : this(
                menuItemId = menuItem.id,
                quantity = 1
        )
}