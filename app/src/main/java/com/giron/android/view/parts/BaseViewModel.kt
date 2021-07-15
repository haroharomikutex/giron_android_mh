package com.giron.android.view.parts

import androidx.lifecycle.ViewModel
import com.giron.android.model.entity.BaseEntity

enum class ViewModelType{
    Giron,
    Girons,
    Comment,
    EmotionCount,
    EmotionAdd,
    Reword,
    Arrows,
    Tag,
    TagCandidate,
    CreateGiron,
    WriteComment,
    SearchCandidateHeader,
    SearchCandidate,
    Notice,
    UserProfile,
    LoyaltyRequestHistory,
    AdvertisedCoin,
    Information;

    // int to EmotionType
    companion object {
        fun fromInt(i: Int): ViewModelType {
            return values().firstOrNull { it.ordinal == i } ?: Giron
        }
    }
}

open class BaseViewModel(val modelType: ViewModelType, open val entity: BaseEntity? = null): ViewModel() {
    open val id = entity?.id
}