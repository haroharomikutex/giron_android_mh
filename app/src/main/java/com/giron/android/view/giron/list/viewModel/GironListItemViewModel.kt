package com.giron.android.view.giron.list.viewModel

import androidx.lifecycle.MutableLiveData
import com.giron.android.model.entity.GironEntity
import com.giron.android.model.net.HttpConstants
import com.giron.android.util.*
import com.giron.android.view.parts.BaseViewModel
import com.giron.android.view.parts.ViewModelType
import com.giron.android.view.giron.detail.viewModel.GironViewModelInterface

/**
 * GironListItemViewModel
 *
 * @param GironEntity
 */
class GironListItemViewModel(var giron: GironEntity) : BaseViewModel(ViewModelType.Girons, giron),
    GironViewModelInterface {
    private val title = giron.title
    val listTitle = "#${giron.id} $title"
    val description = giron.description
    private val createAt = giron.createdAt.toDate()
    var coinCount: MutableLiveData<String> = MutableLiveData(giron.allCoinCount.toString())
    val userImageUrl = HttpConstants.urlBase() + "users/${giron.user.uuid}/image_data/"
    val lastComment = giron.lastComment?.comment ?: ""
    val hasLast = giron.lastComment?.comment != null
    val uuid = giron.user.uuid
    val emotions = createEmotions(giron.emotions, giron.IsEmotions)

    val emotionLabel: String
    get() {
        val count = emotions.sumBy { it.count }
        var icons = ""
        emotions.forEach{ icons += it.type.icon }

        if (icons == "")
            icons = "👍"

        return "$icons $count"
    }

    /**
     * nameLabel
     *
     * @return String
     */
    fun nameLabel(): String {
        val username = giron.user.username
        val views = giron.viewCount

        createAt?.let {
            if (it.isSinceYesterday()) {
                val createdDateStr = createAt.toLabel()
                return "$username・${views}Views・$createdDateStr"
            }
        }
        return "$username・${views}Views"
    }

    companion object {
        private const val TAG = "GironViewModel"
    }
}