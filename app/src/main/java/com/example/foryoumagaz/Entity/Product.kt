package com.example.foryoumagaz.Entity

data class Product(
    val id: Int,
    val name: String,
    val img: Int,
    val count: Int,
    var isVisible: Int = 1
)