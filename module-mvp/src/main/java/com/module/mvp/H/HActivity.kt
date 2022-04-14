package com.module.mvp.H

import com.module.mvp.BaseActivity

class HActivity : BaseActivity<HPresenter>(), HContract.View {

    override var mPresenter: HPresenter?
        get() = HPresenter()
        set(value) {}

    override fun getLayoutId(): Int {
        TODO("Not yet implemented")
    }

    override fun getIsV() {
        TODO("Not yet implemented")
    }



}