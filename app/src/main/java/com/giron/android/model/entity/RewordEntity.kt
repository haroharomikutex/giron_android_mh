package com.giron.android.model.entity

data class RewordEntity (
        override val id: Int,
        val num: Int,
        val type: Int,
        val icon: String
) : BaseEntity