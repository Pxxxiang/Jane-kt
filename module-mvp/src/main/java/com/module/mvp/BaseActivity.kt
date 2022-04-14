package com.module.mvp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<P : IPresenter<IView>> : AppCompatActivity(), IView{

    protected abstract var mPresenter: P?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        mPresenter?.attachView(this)
    }

    protected abstract fun getLayoutId(): Int
}