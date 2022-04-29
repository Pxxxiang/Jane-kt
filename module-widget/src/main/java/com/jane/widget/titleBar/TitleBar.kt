package com.jane.widget.titleBar

import android.R.attr.duration
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Handler
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.TextViewCompat
import com.jane.widget.R
import kotlin.math.max


/**
 * 带动画的titleBar
 * @author liuxin
 * @since 2022/4/27
 */
class TitleBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val DURATION = 300L

    // 操作的view
    private lateinit var mGoBackView: ImageView
    private lateinit var mRightImageView: ImageView
    private lateinit var mTitleView: TextView

    // 属性-默认数值
    private var mTextSize = 26f
    private var mIsTextBold = true
    private var mIsImageDrawableColorUnified = false
    private var mImageUnifiedColor: Int = Color.BLACK
    private var mRightDrawable: Drawable? = null
    private var mGoBackDrawable: Drawable? = context.getDrawable(R.drawable.ic_goback)
    private var mShowAnimateType = AnimatorShowType.NONE
    private var mTitleText: String? = ""
    private var mTitleColor = Color.BLACK

    private fun initAttrs(attrs: AttributeSet) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleBar)
        mTextSize =
            typedArray.getDimensionPixelSize(R.styleable.TitleBar_tb_text_size, mTextSize.toInt())
                .toFloat()
        mIsTextBold = typedArray.getBoolean(R.styleable.TitleBar_tb_text_is_bold, mIsTextBold)
        mTitleText = typedArray.getString(R.styleable.TitleBar_tb_title_text)
        mTitleColor = typedArray.getColor(R.styleable.TitleBar_tb_title_color, mTitleColor)
        mIsImageDrawableColorUnified = typedArray.getBoolean(
            R.styleable.TitleBar_tb_is_image_drawable_color_unified,
            mIsImageDrawableColorUnified
        )
        mImageUnifiedColor = typedArray.getColor(
            R.styleable.TitleBar_tb_image_unified_color,
            mImageUnifiedColor
        )
        mRightDrawable = typedArray.getDrawable(R.styleable.TitleBar_tb_rightView_drawable)

        mShowAnimateType = when (typedArray.getInt(R.styleable.TitleBar_tb_show_animate_type, 0)) {
            1 -> {
                AnimatorShowType.ROTATION_ALPHA_SHOW
            }
            2 -> {
                AnimatorShowType.TRANSLATE_SHOW
            }
            else -> {
                AnimatorShowType.NONE
            }
        }
        typedArray.recycle()
    }

    private fun initView(context: Context) {
        // 后退按钮
        val goBackLP =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT).apply {
                addRule(CENTER_VERTICAL)
                if (mShowAnimateType == AnimatorShowType.ROTATION_ALPHA_SHOW)
                    addRule(ALIGN_PARENT_RIGHT)
            }
        mGoBackView = ImageView(context)
        mGoBackView.setImageDrawable(context.getDrawable(R.drawable.ic_goback))
        mGoBackView.setPadding(16, 8, 16, 8)
        mGoBackView.rotation =
            if (mShowAnimateType == AnimatorShowType.ROTATION_ALPHA_SHOW) 180f else 0f
        addView(mGoBackView, goBackLP)

        // 标题按钮
        val titleLP = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply {
            addRule(CENTER_IN_PARENT)
        }
        mTitleView = TextView(context)
        mTitleView.isSingleLine = true
        mTitleView.gravity = Gravity.CENTER
        mTitleView.alpha =
            if (mShowAnimateType == AnimatorShowType.ROTATION_ALPHA_SHOW) 0f else 1f
        addView(mTitleView, titleLP)

        // 右侧按钮
        val rightImageLP =
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT).apply {
                addRule(CENTER_VERTICAL)
                addRule(ALIGN_PARENT_RIGHT)
            }
        mRightImageView = ImageView(context)
        mRightImageView.alpha =
            if (mShowAnimateType == AnimatorShowType.ROTATION_ALPHA_SHOW) 0f else 1f
        mRightImageView.setPadding(16, 8, 16, 8)
        addView(mRightImageView, rightImageLP)

        // 设置属性值到view中
        setTitleColor(mTitleColor)
        setTitleText(mTitleText, mIsTextBold, mTextSize)
        setImageDrawable(mRightDrawable, mRightImageView)
        setImageDrawable(mGoBackDrawable, mGoBackView)

    }

    /**
     * 展示动画
     */
    private fun rotationAlphaShow() {

        // 返回按钮旋转180°
        mGoBackView.animate()
            .translationX((-width + mGoBackView.measuredWidth).toFloat())
            .rotation(-360f)
            .setDuration(DURATION)
            .start()

        // 透明度
        mTitleView.animate().alpha(1f).setDuration(DURATION * 2).start()
        mRightImageView.animate().alpha(1f).setDuration(DURATION * 2).start()

    }

    /**
     * 展示动画
     */
    private fun translationAndAlphaShow() {

        // 平移
        mGoBackView.animate()
            .translationXBy((mGoBackView.measuredWidth).toFloat())
            .setDuration(DURATION)
            .start()

        mTitleView.animate()
            .translationYBy((mTitleView.measuredHeight).toFloat())
            .setDuration(DURATION)
            .start()
        mRightImageView.animate()
            .translationXBy((-mRightImageView.measuredWidth).toFloat())
            .setDuration(DURATION)
            .start()

    }

    /**返回按钮点击监听*/
    fun setOnGoBackViewClick(listener: OnClickListener) {
        mGoBackView.setOnClickListener(listener)
    }

    /**右侧按钮点击监听*/
    fun setOnRightViewClick(listener: OnClickListener) {
        mRightImageView.setOnClickListener(listener)
    }

    /**返回按钮显示状态*/
    fun setGoBackViewVisibility(visibility: Int) {
        mGoBackView.visibility = visibility
    }

    /**右侧按钮显示状态*/
    fun setRightViewVisibility(visibility: Int) {
        mRightImageView.visibility = visibility
    }

    /**设置标题TitleText的属性*/
    fun setTitleText(title: String?, isBold: Boolean = true, textSize: Float = 18f) {
        mTitleView.text = title
        mTitleView.typeface = if (isBold) Typeface.DEFAULT_BOLD else Typeface.DEFAULT
        mTitleView.textSize = textSize
    }

    /**设置阴影*/
    fun setTitleTextShadowLayer() {
        // 建议别用,text为中文时就是在糊屎
        mTitleView.setShadowLayer(5f, 0f, 0f, Color.parseColor("#ffffff00"))
    }

    /**TitleTextSize自大小动适配*/
    fun setTitleTextSize(minSize: Int, maxSize: Int) {
        if (minSize <= maxSize) {
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                mTitleView, minSize, maxSize, max(
                    1, TextViewCompat.getAutoSizeStepGranularity(
                        mTitleView
                    )
                ), TypedValue.COMPLEX_UNIT_PX
            )
        } else {
            throw RuntimeException("The maxSize value is less than the minSize value")
        }
    }

    /**设置文字颜色*/
    fun setTitleColor(color: Int) {
        mTitleView.setTextColor(color)
    }

    /**设置图标*/
    fun setImageDrawable(drawable: Drawable?, view: ImageView) {
        if (drawable == null) {
            view.visibility = GONE
            return
        }
        if (mIsImageDrawableColorUnified)
            view.setImageDrawable(changeDrawableTint(drawable, mImageUnifiedColor))
        else
            view.setImageDrawable(drawable)
    }


    /**切换图片资源的颜色*/
    fun changeDrawableTint(drawable: Drawable, color: Int): Drawable {
        // drawable.mutate()防止一个屏幕里同一个图片使用了多次
        val wrappedDrawable: Drawable = DrawableCompat.wrap(drawable.mutate())
        DrawableCompat.setTint(wrappedDrawable, color)
        return wrappedDrawable
    }

    /**重新加载布局(动画)*/
    fun reshowAnimal() {
        mTitleView.animate().cancel()
        mRightImageView.animate().cancel()
        mGoBackView.animate().cancel()

        requestLayout()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        if (mShowAnimateType == AnimatorShowType.ROTATION_ALPHA_SHOW)
            Handler().postDelayed({ rotationAlphaShow() }, 300)
        else if (mShowAnimateType == AnimatorShowType.TRANSLATE_SHOW) {
            mRightImageView.translationX = (mRightImageView.measuredWidth).toFloat()
            mTitleView.translationY = (-mTitleView.measuredHeight).toFloat()
            mGoBackView.translationX = (-mGoBackView.measuredWidth).toFloat()
            Handler().postDelayed({ translationAndAlphaShow() }, 300)
        }
    }


    /**获取右侧图片的View*/
    fun getRightImageView(): ImageView {
        return mRightImageView
    }

    /**获取标题的View*/
    fun getTitleView(): TextView {
        return mTitleView
    }

    /**获取返回的View*/
    fun getGoBackView(): ImageView {
        return mGoBackView
    }


    init {
        if (attrs != null) {
            initAttrs(attrs)
        }
        initView(context)
    }

    /*Show类型*/
    enum class AnimatorShowType {
        NONE,
        ROTATION_ALPHA_SHOW,
        TRANSLATE_SHOW,
    }
}