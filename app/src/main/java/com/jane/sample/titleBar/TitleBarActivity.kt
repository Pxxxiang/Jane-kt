package com.jane.sample.titleBar

import android.app.Activity
import android.os.Bundle
import com.jane.sample.R
import com.jane.widget.titleBar.TitleBar

class TitleBarActivity : Activity(){
    private lateinit var mTitleBar: TitleBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_titlebar)

        actionBar!!.hide()
        mTitleBar = findViewById(R.id.titleBar)
        mTitleBar.setOnGoBackViewClick { finish() }
        mTitleBar.setOnRightViewClick { mTitleBar.reshowAnimal() }
        mTitleBar.getGoBackView()
    }


}