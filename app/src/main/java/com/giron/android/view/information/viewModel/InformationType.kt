@file:Suppress("KDocUnresolvedReference")

package com.giron.android.view.information.viewModel

/**
 * InformationType
 *
 * @param Int
 */
enum class InformationType(val value: Int) {
    None(0),
    Url(1),
    RequiredUrl(2),
    Giron(3),
    Comment(4),
    Loyalty(5);

    /**
     * static
     */
    companion object {
        /**
         * fromInt
         *
         * @param Int
         *
         * @return InformationType
         */
        fun fromInt(value: Int): InformationType {
            return values()
                .firstOrNull(){it.value == value} ?: None
        }
    }
}