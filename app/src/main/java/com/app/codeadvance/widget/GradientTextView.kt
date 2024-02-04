package com.app.codeadvance.widget

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import androidx.annotation.IntDef
import com.app.codeadvance.R

class GradientTextView : View {
    private var text: String? = "渐变文本"
    private var textSize = sp2px(20f)
    private var textColor = Color.BLACK
    private var textColorChange = Color.RED
    private var mProgress: Float = 0f

    @Directions
    private var direction = DIRECTION_LEFT

    @IntDef(flag = true, value = [DIRECTION_LEFT, DIRECTION_RIGHT, DIRECTION_TOP, DIRECTION_BOTTOM])
    @Retention(
        AnnotationRetention.SOURCE
    )
    annotation class Directions

    private val textBound = Rect()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var textWidth = 0
    private val textHeight = 0
    private var textStartX = 0
    private var textStartY = 0

    constructor(context: Context?) : super(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttr(context, attrs)
    }

    private fun initAttr(context: Context, attrs: AttributeSet?) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.GradientTextView)
        text = ta.getString(R.styleable.GradientTextView_text)
        textSize = ta.getDimensionPixelSize(R.styleable.GradientTextView_text_size, textSize)
        textColor = ta.getColor(R.styleable.GradientTextView_text_color, textColorChange)
        textColorChange =
            ta.getColor(R.styleable.GradientTextView_text_color_change, textColorChange)
        mProgress = ta.getFloat(R.styleable.GradientTextView_progress, 0f)
        direction = ta.getInt(R.styleable.GradientTextView_direction, direction)
        ta.recycle()
        paint.textSize = textSize.toFloat()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        paint.textSize = textSize.toFloat()
        // 1.先测量文字
        measureText()
        // 2.测量自身
        val width = measureWidth(widthMeasureSpec)
        val height = measureHeight(heightMeasureSpec)
        // 3.保持测量尺寸
        setMeasuredDimension(width, height)
        textStartX = measuredWidth / 2 - textWidth / 2
        textStartY = measuredHeight / 2 - textHeight / 2
    }

    private fun measureText() {
        paint.getTextBounds(text, 0, text!!.length, textBound)
        textWidth = (paint.measureText(text) + .5f).toInt()
    }

    private fun measureWidth(measureSpec: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        var result = 0
        when (mode) {
            MeasureSpec.EXACTLY -> result = size
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> result =
                (textWidth + .5f + paddingLeft + paddingRight).toInt()
        }
        // 如果是AT_MOST,不能超过父布局的尺寸
        result = if (mode == MeasureSpec.AT_MOST) Math.min(result, size) else result
        return result
    }

    private fun measureHeight(measureSpec: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        var result = 0
        when (mode) {
            MeasureSpec.EXACTLY -> result = size
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> result =
                (textWidth + .5f + paddingTop + paddingBottom).toInt()
        }
        // 如果是AT_MOST,不能超过父布局的尺寸
        result = if (mode == MeasureSpec.AT_MOST) Math.min(result, size) else result
        return result
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (direction) {
            DIRECTION_LEFT -> {
                // 1. 绘制改变的颜色的文字
                paint.color = textColorChange
                // 起始
                // 绘制的终点
                canvas.save()
                canvas.clipRect(
                    textStartX.toFloat(),
                    0f,
                    textStartX + mProgress * textWidth,
                    measuredHeight.toFloat()
                )
                canvas.drawText(
                    text!!,
                    textStartX.toFloat(),
                    measuredHeight / 2 - (paint.descent() / 2 + paint.ascent() / 2),
                    paint
                )
                canvas.restore()

                // 2. 绘制文字的底色
                paint.color = textColor
                canvas.save()
                canvas.clipRect(
                    textStartX + mProgress * textWidth,
                    0f,
                    (textStartX + textWidth).toFloat(),
                    measuredHeight.toFloat()
                )
                canvas.drawText(
                    text!!,
                    textStartX.toFloat(),
                    measuredHeight / 2 - (paint.descent() / 2 + paint.ascent() / 2),
                    paint
                )
                canvas.restore()
            }

            DIRECTION_RIGHT -> {
                // 先绘制改变的颜色
                drawTextHorizontal(
                    canvas,
                    textColorChange,
                    (textStartX + (1 - mProgress) * textWidth).toInt(),
                    textStartX + textWidth
                )
                // 后绘制底色
                drawTextHorizontal(
                    canvas,
                    textColor,
                    textStartX,
                    (textStartX + (1 - mProgress) * textWidth).toInt()
                )
            }

            DIRECTION_TOP -> {
                drawTextVertical(
                    canvas,
                    textColorChange,
                    textStartY,
                    (textStartY + mProgress * textHeight).toInt()
                )
                drawTextVertical(
                    canvas,
                    textColor,
                    (textStartY + mProgress * textHeight).toInt(),
                    textStartY + textHeight
                )
            }

            DIRECTION_BOTTOM -> {
                drawTextVertical(
                    canvas,
                    textColorChange,
                    (textStartY + 1 - mProgress * textHeight).toInt(),
                    textStartY + textHeight
                )
                drawTextVertical(
                    canvas,
                    textColor,
                    textStartY,
                    (textStartY + (1 - mProgress) * textHeight).toInt()
                )
            }

            else -> {}
        }
    }

    private fun drawTextHorizontal(canvas: Canvas, color: Int, startX: Int, endX: Int) {
        paint.color = color
        canvas.save()
        canvas.clipRect(startX, 0, endX, measuredHeight)
        canvas.drawText(
            text!!,
            textStartX.toFloat(),
            measuredHeight / 2 - (paint.descent() + paint.ascent()) / 2,
            paint
        )
        canvas.restore()
    }

    private fun drawTextVertical(canvas: Canvas, color: Int, startY: Int, endY: Int) {
        paint.color = color
        canvas.save()
        canvas.clipRect(0, startY, measuredWidth, endY)
        canvas.drawText(
            text!!,
            textStartX.toFloat(),
            measuredHeight / 2 - (paint.descent() + paint.ascent()) / 2,
            paint
        )
        canvas.restore()
    }

    fun getTextSize(): Int {
        return textSize
    }

    fun setTextSize(textSize: Int) {
        this.textSize = textSize
        paint.textSize = textSize.toFloat()
        requestLayout()
        invalidate()
    }

    fun setText(text: String?) {
        this.text = text
        requestLayout()
        invalidate()
    }

    fun getTextColorChange(): Int {
        return textColorChange
    }

    fun setTextColorChange(textColorChange: Int) {
        this.textColorChange = textColorChange
        invalidate()
    }

    fun getTextColor(): Int {
        return textColor
    }

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
        invalidate()
    }

    fun getProgress(): Float {
        return mProgress
    }

    fun setProgress(progress: Float) {
        this.mProgress = progress
        invalidate()
    }

    fun setDirection(@Directions direction: Int) {
        this.direction = direction
    }

    companion object {
        const val DIRECTION_LEFT = 0
        const val DIRECTION_RIGHT = 1
        const val DIRECTION_TOP = 2
        const val DIRECTION_BOTTOM = 3
        fun sp2px(sp: Float): Int {
            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                Resources.getSystem().displayMetrics
            ).toInt()
        }
    }
}
