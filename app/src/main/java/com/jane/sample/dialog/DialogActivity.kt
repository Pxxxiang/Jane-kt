package com.jane.sample.dialog

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import com.jane.sample.R

class DialogActivity : Activity(), MessageDialog.NegativeCallBack, MessageDialog.PositiveCallBack {
    private lateinit var mTitleBar: TitleBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)

        actionBar!!.hide()
        mTitleBar = findViewById(R.id.titleBar)
        mTitleBar.setOnGoBackViewClick { finish() }
        mTitleBar.setOnRightViewClick { mTitleBar.reshowAnimal() }

        findViewById<Button>(R.id.button_show_bottom).setOnClickListener {
            MessageDialog(this)
                .setMessage(getString(R.string.dialog_show_bottom))
                .setGravity(Gravity.BOTTOM)
                .setNegativeText(getString(R.string.cancel), this, true)
                .setPositiveText(getString(R.string.ok), this)
                .setTitle(getString(R.string.dialog_show_bottom))
                .show(false)
        }

        findViewById<Button>(R.id.button_show_center).setOnClickListener {
            MessageDialog(this)
                .setTitle(getString(R.string.dialog_show_center))
                .setGravity(Gravity.CENTER)
                .setNegativeText(getString(R.string.cancel), this)
                .setPositiveText(getString(R.string.ok), this, true)
                .setMessage(getString(R.string.dialog_show_center))
                .show(false)
        }

        findViewById<Button>(R.id.button_error_shake_show).setOnClickListener {
            MessageDialog(this)
                .setTitle(getString(R.string.dialog_animal_error_shake))
                .animateErrorShow(1)
        }

        findViewById<Button>(R.id.button_alpha_show).setOnClickListener {
            MessageDialog(this)
                .setTitle(getString(R.string.dialog_animal_alpha))
                .animateAlphaShow()
        }

        findViewById<Button>(R.id.button_animal_combination_show).setOnClickListener {
            MessageDialog(this)
                .setTitle(getString(R.string.dialog_animal_combination))
                .animationSetShow()
        }

        findViewById<Button>(R.id.button_animal_translate_show).setOnClickListener {
            MessageDialog(this)
                .setTitle(getString(R.string.dialog_animal_translate))
                .setGravity(Gravity.BOTTOM)
                .animateTranslateShow()
        }
    }

    override fun onNegativeClick(): MessageDialog.AnimatorDismissType {
        return MessageDialog.AnimatorDismissType.ALPHA_DISMISS
//      return MessageDialog.AnimatorDismissType.TRANSLATE_DISMISS
//        return MessageDialog.AnimatorDismissType.NORMAL
    }

    override fun onPositiveClick(): MessageDialog.AnimatorDismissType {
//      return MessageDialog.AnimatorDismissType.ALPHA_DISMISS
        return MessageDialog.AnimatorDismissType.TRANSLATE_DISMISS
//        return MessageDialog.AnimatorDismissType.NORMAL
    }
}