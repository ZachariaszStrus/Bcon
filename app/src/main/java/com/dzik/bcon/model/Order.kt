package com.dzik.bcon.model


data class Order (

        val fcmToken: String,

        val beaconUID: BeaconUID,

        var orderItemRequestList: List<OrderItem>
) {
        constructor(menuItems: Map<MenuItem, Int>, beaconUID: BeaconUID, fcmToken: String) : this(
                beaconUID = beaconUID,
                orderItemRequestList = menuItems.map { OrderItem(it.key.id, it.value) },
                fcmToken = fcmToken
        )
}