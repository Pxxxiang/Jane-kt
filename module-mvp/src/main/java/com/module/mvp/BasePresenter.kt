package com.module.mvp

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner

open class BasePresenter<out V : IView>: IPresenter<V> {

    protected lateinit var mvpView: IView

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        TODO("Not yet implemented")
    }

    override fun attachView(mvpView: IView) {
        this.mvpView = mvpView
    }

    override fun detachView(mvpView: IView) {
        TODO("Not yet implemented")
    }
}