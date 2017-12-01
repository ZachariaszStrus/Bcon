package com.dzik.bcon.model


data class Order (

        val fcmToken: String,

        val beaconUID: BeaconUID,

        var orderItemRequestList: List<OrderItem>
) {
        constructor(menuItems: List<MenuItem>, beaconUID: BeaconUID, fcmToken: String) : this(
                beaconUID = beaconUID,
                orderItemRequestList = menuItems.map { OrderItem(it) },
                fcmToken = fcmToken
        )
}