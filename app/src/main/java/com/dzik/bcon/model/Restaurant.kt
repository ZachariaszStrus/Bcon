package com.dzik.bcon.model


class Restaurant (

        val id: Int,

        val name: String,

        val menu: List<MenuItem>,

        val paymentOptions: MutableList<PaymentOption>,

        val imageUrl: String

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Restaurant

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id
    }
}