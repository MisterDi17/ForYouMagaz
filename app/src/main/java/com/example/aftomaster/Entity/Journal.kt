package com.example.aftomaster.Entity

data class Journal(
    val id : Int,
    val product_id: Int,
    val product_count: Int,
    val order_data: String,
    var chek: Int = 0
)
