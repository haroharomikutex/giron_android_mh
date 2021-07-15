@file:Suppress("KDocUnresolvedReference")

package com.giron.android.extension

import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.giron.android.GironsNavigationDirections
import com.giron.android.model.dao.Transition
import com.giron.android.model.dao.TransitionType
import com.giron.android.view.main.MainActivity

/**
 * Fragment.closeKeyboard
 * Retract the keyboard
 */
fun Fragment.closeKeyboard() {
    view?.let {v ->
        val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

/**
 * mainActivity
 */
val Fragment.mainActivity: MainActivity
    get() {
        return activity as MainActivity
    }

fun Fragment.setOption(isActiveCall: Boolean = true) {
    mainActivity.setFragment(this, isActiveCall)
}

fun Fragment.navigate(directions: NavDirections) {
    view?.post {
        findNavController().navigate(directions)
    } ?: run {
        Log.d("Fragment", "View is not post. Navigation failed. ")
    }
}

fun Fragment.navigateUp() {
    view?.post {
        findNavController().navigateUp()
    } ?: run {
        Log.d("Fragment", "View is not post. Navigation failed. ")
    }
}

 fun Fragment.transition(transition: Transition) {
    if (transition.type == TransitionType.NONE)
        return

    when(transition.type) {
        TransitionType.GIRON -> {
            val action = GironsNavigationDirections.actionGlobalNavigation()
            action.id = transition.gironId
            action.num = transition.commentNum
            navigate(action)
        }
        TransitionType.WEB -> {
            val action = GironsNavigationDirections.actionGlobalWebFragment()
            action.url = transition.url ?: ""
            navigate(action)
        }
        TransitionType.REQUIRED_WEB -> {
            val action = GironsNavigationDirections.actionGlobalWebFragment()
            action.url = transition.url ?: ""
            action.required = true
            navigate(action)
        }
        TransitionType.MYPAGE -> {
            val action = GironsNavigationDirections.actionGlobalMyPageFragment()
            navigate(action)
        }
        TransitionType.COIN -> {
            val action = GironsNavigationDirections.actionGlobalCoinFragment()
            navigate(action)
        }
        else -> return
    }
}