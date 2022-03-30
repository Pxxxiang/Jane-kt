package com.jane.sample.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.*
import android.widget.Button
import android.widget.TextView
import com.jane.sample.R

/**
 * 消息提示提示MessageDialog
 */
class MessageDialog(context: Context) : Dialog(context, R.style.Dialog) {

    private var mTitleView: TextView? = null
    private var mMessageView: TextView? = null
    private var mNegativeButton: Button? = null
    private var mPositiveButton: Button? = null
    private var mCenterView: View? = null

    private var mTitle: String? = null
    private var mMessage: String? = null
    private var mNegativeText: String? = null
    private var mPositiveText: String? = null
    private var isNegativeError: Boolean = false
    private var isPositiveError: Boolean = false
    private var mNegativeCallBack: NegativeCallBack? = null
    private var mPositiveCallBack: PositiveCallBack? = null
    private var mWindowAlpha = 0.92f
    private var isUseAnimate: Boolean = false
    private var mDuration: Long = 600
    private var mGravity = Gravity.CENTER

    private var mContentView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentView = LayoutInflater.from(context).inflate(R.layout.dialog_message, null);
        setContentView(mContentView!!)
        mTitleView = findViewById(R.id.textView_messageDialog_title)
        mMessageView = findViewById(R.id.textView_messageDialog_message)
        mNegativeButton = findViewById(R.id.button_messageDialog_negative)
        mPositiveButton = findViewById(R.id.button_messageDialog_positive)
        mCenterView = findViewById(R.id.view_messageDialog_center)
        mPositiveButton!!.setOnClickListener {
            if (mPositiveCallBack != null) {
                mPositiveCallBack!!.onPositiveClick()
                if (isUseAnimate)
                    animateDismiss()
                else dismiss()
            }
        }
        mNegativeButton!!.setOnClickListener {
            if (mNegativeCallBack != null) {
                mNegativeCallBack!!.onNegativeClick()
                if (isUseAnimate)
                    animateDismiss()
                else dismiss()
            }
        }

    }

    fun setTitle(title: String?): MessageDialog {
        mTitle = title
        return this
    }

    fun setMessage(message: String?): MessageDialog {
        mMessage = message
        return this
    }

    fun setNegativeText(
        str: String?,
        negativeCallBack: NegativeCallBack?,
        isError: Boolean = false
    ): MessageDialog {
        mNegativeText = str
        mNegativeCallBack = negativeCallBack
        isNegativeError = isError
        return this
    }

    fun setPositiveText(
        str: String?,
        positiveCallBack: PositiveCallBack?,
        isError: Boolean = false
    ): MessageDialog {
        mPositiveText = str
        mPositiveCallBack = positiveCallBack
        isPositiveError = isError
        return this
    }

    fun setAlpha(float: Float): MessageDialog {
        mWindowAlpha = float
        if (mWindowAlpha > 1) mWindowAlpha = 1f
        if (mWindowAlpha < 0) mWindowAlpha = 0f
        return this
    }

    fun isUseAnimate(isUse: Boolean, duration: Long): MessageDialog {
        isUseAnimate = isUse
        mDuration = duration
        return this
    }

    fun setGravity(gravity: Int): MessageDialog {
        mGravity = gravity
        return this
    }

    override fun show() {
        super.show()
        mTitleView!!.text = mTitle
        mMessageView!!.text = mMessage
        mPositiveButton!!.text = mPositiveText
        mNegativeButton!!.text = mNegativeText

        if (mNegativeText == null || mNegativeText!!.isEmpty()) {
            mNegativeButton!!.visibility = View.GONE
            mCenterView!!.visibility = View.GONE
        } else {
            mNegativeButton!!.visibility = View.VISIBLE
            mCenterView!!.visibility = View.VISIBLE
        }

        if (isNegativeError)
            mNegativeButton!!.setTextColor(context.resources.getColor(R.color.messageDialog_button_text_fail_color))

        if (isPositiveError)
            mPositiveButton!!.setTextColor(context.resources.getColor(R.color.messageDialog_button_text_fail_color))

        val lp: WindowManager.LayoutParams = window!!.attributes
        lp.alpha = mWindowAlpha
        window!!.attributes = lp
        window!!.setGravity(mGravity)

        animateShow()
    }

    private fun animateShow() {
        if (mContentView == null || !isUseAnimate) {
            return
        }
        val translate = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f
        )
        val alpha = AlphaAnimation(0f, 1f)
        val set = AnimationSet(true)
        set.addAnimation(translate)
        set.addAnimation(alpha)
        set.interpolator = DecelerateInterpolator()
        set.duration = mDuration
        set.fillAfter = true
        mContentView!!.startAnimation(set)
    }

    private fun animateDismiss() {
        if (mContentView == null || !isUseAnimate) {
            return
        }
        val translate = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f
        )
        val alpha = AlphaAnimation(1f, 0f)
        val set = AnimationSet(true)
        set.addAnimation(translate)
        set.addAnimation(alpha)
        set.interpolator = DecelerateInterpolator()
        set.duration = mDuration
        set.fillAfter = true
        set.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                dismiss()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        mContentView!!.startAnimation(set)
    }

    interface NegativeCallBack {
        fun onNegativeClick()
    }

    interface PositiveCallBack {
        fun onPositiveClick()
    }
}