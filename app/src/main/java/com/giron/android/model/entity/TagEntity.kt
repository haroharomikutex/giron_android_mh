package com.giron.android.model.entity

/**
 * TagEntity
 */
data class TagEntity(
        override val id: Int,
        val name: String,
        val isLock: Boolean) : BaseEntity {
    /**
     * toString
     *
     * @return String
     */
    override fun toString() : String {
        var result = "#$name"
        if(isLock) result += "🔒"
        return result
    }
}
