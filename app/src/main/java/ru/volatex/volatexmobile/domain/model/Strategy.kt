package ru.volatex.volatexmobile.domain.model

data class Strategy(
    val ID: Int,
    val UserID: String,
    val Figi: String,
    val BuyPrice: Double,
    val BuyQuantity: Int,
    val SellPrice: Double,
    val SellQuantity: Int,
    val CreatedAt: String,
    val UpdatedAt: String
)

data class AddStrategyRequest(
    val buy_price: Double,
    val buy_quantity: Int,
    val figi: String,
    val sell_price: Double,
    val sell_quantity: Int
)
