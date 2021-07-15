@file:Suppress("KDocUnresolvedReference", "unused")

package com.giron.android.view.giron.detail.viewModel

import com.giron.android.view.parts.BaseViewModelList

/**
 * EmotionType
 *
 * @param Int num
 * @param String print icon
 */
enum class EmotionType(val num: Int, val icon: String) {
    GOOD(0, "👍"),
    EYE(1, "👀"),
    THINK(2, "🤔"),
    LOVE(3, "😍"),
    WORRY(4, "😥");

    /**
     * static
     */
    companion object {
        /**
         * fromInt
         * intをEmotionに変換する
         *
         * @param Int
         * @return EmotionType
         */
        fun fromInt(i: Int): EmotionType {
            return values().firstOrNull { it.num == i } ?: GOOD
        }

        /**
         * createViewModelList
         * エモーションの配列
         *
         * @return BaseViewModelList
         */
        fun createViewModelList() : BaseViewModelList {
            val list = BaseViewModelList()
            val add = values().map { EmotionViewModel(it.num, 0, false) }
            list.items.addAll(add)
            return list
        }
    }
}