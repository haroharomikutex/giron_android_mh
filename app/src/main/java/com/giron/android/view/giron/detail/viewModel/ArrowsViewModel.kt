package com.giron.android.view.giron.detail.viewModel

import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.ViewModelType

class ArrowsViewModel(val gironDetal: GironDetailViewModel) : BaseViewModel(ViewModelType.Arrows) {
    private val currentPage = gironDetal.giron.currentPage
    private val maxPage = gironDetal.giron.maxPage
    val printPage = "${currentPage + 1} / ${maxPage + 1}"
    val isFirstEnable = currentPage > 0
    val isPrevEnable = currentPage > 0
    val isNextEnable = currentPage < gironDetal.giron.maxPage
    val isLastEnable = currentPage < gironDetal.giron.maxPage
    val isFirstLastVisible = maxPage > 2

    fun first() {
        if (isFirstEnable)
            gironDetal.getCommentByPage(0)
    }

    fun prev() {
        if (isPrevEnable)
            gironDetal.getCommentByPage(currentPage - 1)
    }

    fun next() {
        if (isNextEnable)
            gironDetal.getCommentByPage(currentPage + 1)
    }

    fun last() {
        if (isLastEnable)
            gironDetal.getCommentByPage(maxPage)
    }
}