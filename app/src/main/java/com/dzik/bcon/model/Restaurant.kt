package com.dzik.bcon.model


data class Restaurant (

        val id: Int,

        val name: String,

        val menu: List<MenuItem>,

        val imageUrl: String
)