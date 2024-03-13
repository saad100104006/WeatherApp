package com.app.weather.infrastructure.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun LiveData<Boolean>.observeAsAction(
    lifecycleOwner: LifecycleOwner,
    action: () -> Unit,
    onActionResolved: () -> Unit
) {
    this.removeObservers(lifecycleOwner)
    this.observe(lifecycleOwner) { isActionRequested ->
        if (isActionRequested == true) {
            action.invoke()
            onActionResolved.invoke()
        }
    }
}

fun <T> LiveData<T?>.observeAsAction(
    lifecycleOwner: LifecycleOwner,
    action: (T) -> Unit,
    onActionResolved: () -> Unit
) {
    this.removeObservers(lifecycleOwner)
    this.observe(lifecycleOwner) { actionParams ->
        if (actionParams != null) {
            action.invoke(actionParams)
            onActionResolved.invoke()
        }
    }
}

fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
    observe(owner, object : Observer<T> {
        override fun onChanged(value: T) {
            observer(value!!)
            removeObserver(this)
        }
    })
}