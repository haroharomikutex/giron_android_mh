package com.giron.android.view.parts.listener

import android.transition.Transition
import androidx.fragment.app.Fragment

interface TopFragmentListener {
    val currentFragment: Fragment
    val isPlayHomeAsUpEnabled: Boolean
    val backStackCount: Int
    val isCreatableGiron: Boolean
    fun back(): Boolean
}