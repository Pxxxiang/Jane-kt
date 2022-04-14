package com.module.mvp.H

import com.module.mvp.IPresenter
import com.module.mvp.IView

interface HContract {

    interface View : IView {

        fun getIsV()
    }

    interface Presenter : IPresenter<View> {

        fun getIsP()
    }
}