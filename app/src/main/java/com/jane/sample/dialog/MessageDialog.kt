package com.jane.sample.dialog

import android.animation.ObjectAnimator
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
 * @author liuxin
 * @since 2022/3/30
 */
class MessageDialog(context: Context) : Dialog(context, R.style.Dialog) {

    private val DURATION_TIME: Long = 600

    private lateinit var mTitleView: TextView
    private lateinit var mMessageView: TextView
    private lateinit var mNegativeButton: Button
    private lateinit var mPositiveButton: Button
    private lateinit var mCenterView: View
    private lateinit var mContentView: View

    private var mTitle: String? = null
    private var mMessage: String? = null
    private var mNegativeText: String? = null
    private var mPositiveText: String? = null
    private var isNegativeError: Boolean = false
    private var isPositiveError: Boolean = false
    private var mNegativeCallBack: NegativeCallBack? = null
    private var mPositiveCallBack: PositiveCallBack? = null
    private var mWindowAlpha = 0.92f
    private var mGravity = Gravity.CENTER


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContentView = LayoutInflater.from(context).inflate(R.layout.dialog_message, null);
        setContentView(mContentView)
        mTitleView = findViewById(R.id.textView_messageDialog_title)
        mMessageView = findViewById(R.id.textView_messageDialog_message)
        mNegativeButton = findViewById(R.id.button_messageDialog_negative)
        mPositiveButton = findViewById(R.id.button_messageDialog_positive)
        mCenterView = findViewById(R.id.view_messageDialog_center)
        mPositiveButton.setOnClickListener {
            if (mPositiveCallBack != null) {
                getTypeToDismiss(mPositiveCallBack!!.onPositiveClick())
            }
        }
        mNegativeButton.setOnClickListener {
            if (mNegativeCallBack != null) {
                getTypeToDismiss(mNegativeCallBack!!.onNegativeClick())

            }
        }

    }

    /**设置标题*/
    fun setTitle(title: String): MessageDialog {
        mTitle = title
        return this
    }

    /**设置内容*/
    fun setMessage(message: String): MessageDialog {
        mMessage = message
        return this
    }

    /**设置取消按钮和回调*/
    fun setNegativeText(
        str: String,
        negativeCallBack: NegativeCallBack?,
        isError: Boolean = false
    ): MessageDialog {
        mNegativeText = str
        mNegativeCallBack = negativeCallBack
        isNegativeError = isError
        return this
    }

    /**设置确定按钮和回调*/
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

    /**设置透明度*/
    fun setAlpha(float: Float): MessageDialog {
        mWindowAlpha = float
        if (mWindowAlpha > 1) mWindowAlpha = 1f
        if (mWindowAlpha < 0) mWindowAlpha = 0f
        return this
    }

    /**设置显示位置*/
    fun setGravity(gravity: Int = Gravity.BOTTOM): MessageDialog {
        mGravity = gravity
        return this
    }

    override fun show() {
        super.show()
        mTitleView.text = mTitle
        mMessageView.text = mMessage
        mPositiveButton.text = mPositiveText
        mNegativeButton.text = mNegativeText

        if (mNegativeText == null || mNegativeText!!.isEmpty()) {
            mNegativeButton.visibility = View.GONE
            mCenterView.visibility = View.GONE
        } else {
            mNegativeButton.visibility = View.VISIBLE
            mCenterView.visibility = View.VISIBLE
        }

        if (isNegativeError)
            mNegativeButton.setTextColor(context.resources.getColor(R.color.messageDialog_button_text_fail_color))

        if (isPositiveError)
            mPositiveButton.setTextColor(context.resources.getColor(R.color.messageDialog_button_text_fail_color))

        val lp: WindowManager.LayoutParams = window!!.attributes
        lp.alpha = mWindowAlpha
        window!!.attributes = lp
        window!!.setGravity(mGravity)
    }

    /**
     * 组合动画show
     *
     * @param duration    持续时间
     * */
    fun animationSetShow(duration: Long = 2000) {
        show()

        val animationSet = AnimationSet(true)

        val alphaAnimation = AlphaAnimation(0f, 1f)
        val rotateAnimation = RotateAnimation(
            0f,
            720f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        val scaleAnimation = ScaleAnimation(
            0.1f, 1f, 0.1f, 1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )

        animationSet.addAnimation(alphaAnimation)
        animationSet.addAnimation(rotateAnimation)
        animationSet.addAnimation(scaleAnimation)
        animationSet.duration = duration
        animationSet.interpolator = AccelerateDecelerateInterpolator()
        animationSet.fillAfter = true

        mContentView.startAnimation(animationSet)

    }

    /**
     * 淡入淡出动画透明度Show
     *
     * @param duration    持续时间
     */
    fun animateAlphaShow(duration: Long = DURATION_TIME) {
        show()

        val animator = AlphaAnimation(0.2f, 1f)
        animator.duration = duration
        mContentView.startAnimation(animator)
    }

    /**
     * 错误动画左右抖动show
     *
     * @param repeatCount 重复次数
     * @param duration    持续时间
     */
    fun animateErrorShow(repeatCount: Int, duration: Long = DURATION_TIME) {
        show()
        val animator = ObjectAnimator.ofFloat(mTitleView, "translationX", -10f, 10f, -10f, 10f)
        animator.duration = duration
        animator.repeatCount = repeatCount
        animator.start()
    }

    /**
     * 弹出动画平移show
     *
     * @param duration    持续时间
     * */
    fun animateTranslateShow(duration: Long = DURATION_TIME) {
        show()

        val translate = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f
        )
        val alpha = AlphaAnimation(0f, 1f)
        val set = AnimationSet(true)
        set.addAnimation(translate)
        set.addAnimation(alpha)
        set.interpolator = DecelerateInterpolator()
        set.duration = duration
        set.fillAfter = true
        mContentView.startAnimation(set)
    }

    /**
     * 关闭动画平移dismiss
     *
     * @param duration    持续时间
     * */
    fun animateTranslateDismiss(duration: Long = DURATION_TIME) {
        val translate = TranslateAnimation(
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f
        )
        val alpha = AlphaAnimation(1f, 0f)
        val set = AnimationSet(true)
        set.addAnimation(translate)
        set.addAnimation(alpha)
        set.interpolator = DecelerateInterpolator()
        set.duration = duration
        set.fillAfter = true
        set.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                dismiss()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        mContentView.startAnimation(set)
    }

    /**
     * 淡入淡出动画透明度Dismiss
     *
     * @param duration    持续时间
     */
    fun animateAlphaDismiss(duration: Long = DURATION_TIME) {
        val animator = AlphaAnimation(1f, 0f)
        animator.duration = duration
        animator.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
            }

            override fun onAnimationEnd(animation: Animation) {
                dismiss()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        mContentView.startAnimation(animator)
    }

    /**
     * 根据枚举去设置dismiss类型
     * */
    private fun getTypeToDismiss(type: AnimatorDismissType) {
        when (type) {
            AnimatorDismissType.ALPHA_DISMISS -> {
                animateAlphaDismiss()
            }
            AnimatorDismissType.TRANSLATE_DISMISS -> {
                animateTranslateDismiss()
            }
            else -> {
                dismiss()
            }
        }
    }

    interface NegativeCallBack {
        fun onNegativeClick(): AnimatorDismissType
    }

    interface PositiveCallBack {
        fun onPositiveClick(): AnimatorDismissType
    }

    /*Dismiss类型*/
    enum class AnimatorDismissType {
        NORMAL,
        ALPHA_DISMISS,
        TRANSLATE_DISMISS,
    }
}