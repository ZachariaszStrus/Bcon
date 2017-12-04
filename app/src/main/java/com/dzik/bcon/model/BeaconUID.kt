package com.dzik.bcon.model


data class BeaconUID (

        val namespace: String,

        val instance: String
) {
    override fun toString(): String = instance
}
