package com.giron.android.view.loyalty.viewModel

/**
 * LoyaltyType
 */
enum class LoyaltyType(val value: String) {
    Amazon("amazon"),
    Quo("quo");

    /**
     * static
     */
    companion object {
        fun fromString(value: String): LoyaltyType {
            return values().firstOrNull(){it.value == value} ?: Amazon
        }
    }
}