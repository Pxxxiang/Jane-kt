package com.module.mvp

import androidx.lifecycle.LifecycleEventObserver

interface IPresenter<out V : IView> : LifecycleEventObserver {

    fun attachView(mvpView: IView)
    fun detachView(mvpView: IView)

}