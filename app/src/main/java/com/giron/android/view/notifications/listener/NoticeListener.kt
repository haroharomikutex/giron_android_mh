package com.giron.android.view.notifications.listener

import com.giron.android.view.notifications.viewModel.NoticeViewModel

interface NoticeListener {
    fun onClickNotice(model: NoticeViewModel)
}