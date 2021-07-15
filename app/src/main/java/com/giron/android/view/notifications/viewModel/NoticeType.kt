package com.giron.android.view.notifications.viewModel

import com.giron.android.view.GironApplication

/**
 * NoticeType
 *
 * @param id
 */
enum class NoticeType(val id: Int) {
    NONE(0),
    COMMENT(1),
    EMOTION(2),
    REWORD(3),
    ADVERTISEMENT(4),
    GIRON(5),
    COIN(6),
    URL(7),
    REQUIEDURL(8),
    MYPAGE(9);

    /**
     * toNoticeIcon
     *
     * @return Int
     */
    fun toNoticeIcon() : Int{
        val iconName = when(this) {
            COMMENT -> "ic_comment"
            REWORD -> "ic_gcoin"
            else -> "ic_icon"
        }

        val context = GironApplication.instance
        return context.resources.getIdentifier(iconName, "drawable", context.packageName)
    }

    /**
     * static
     */
    companion object {
        fun fromInt(id: Int): NoticeType {
            return values().firstOrNull { it.id == id } ?: NONE
        }
    }
}