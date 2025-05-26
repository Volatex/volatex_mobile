package ru.volatex.volatexmobile.domain.model

data class Position(
    val figi: String,
    val ticker: String,
    val name: String,
    val quantity: Int,
    val current_price: Double,
    val total_value: Double,
    val commission: Double,
    val volatility: String
)
