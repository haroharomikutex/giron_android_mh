@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.loyalty.viewModel

import com.giron.android.extension.locate

/**
 * LoyaltyStateType
 *
 * @param Int
 */
enum class LoyaltyStateType(val value: Int) {
    None(0),
    OK(1),
    Cancel(2);

    /**
     * toString
     */
    override fun toString(): String {
        return when(this) {
            None -> "loyalty_request_state_none".locate()
            OK -> "loyalty_request_state_ok".locate()
            Cancel -> "loyalty_request_state_cancel".locate()
        }
    }

    /**
     * static
     */
    companion object {
        /**
         * fromInt
         *
         * @param Int
         *
         * @return LoyaltyStateType
         */
        fun fromInt(value: Int): LoyaltyStateType {
            return values().firstOrNull(){it.value == value} ?: None
        }
    }
}