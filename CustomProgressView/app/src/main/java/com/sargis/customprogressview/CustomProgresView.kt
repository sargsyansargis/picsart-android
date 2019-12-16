package com.sargis.customprogressview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.FloatRange
import kotlin.math.min

class CustomProgressView(context: Context, attrs: AttributeSet?) :
    View(context, attrs, 0) {

    private var startColor = 0
    private var endColor = 0

    private var strokePaint: Paint = Paint().apply {
        strokeCap = Paint.Cap.ROUND
        isAntiAlias = true
        strokeWidth = 1f
        color = Color.GREEN
    }

    private var borderPaint: Paint = Paint().apply {
        strokeCap = Paint.Cap.ROUND
        strokeWidth = 1f
        isAntiAlias = true
        color = Color.GRAY
    }

    private var strokeWidth: Int = 30
        set(value) {
            field = value
            borderPaint.strokeWidth = value.toFloat()
            strokePaint.strokeWidth = value.toFloat()
            setStrokeColor(startColor, endColor)
        }

    private var progress: Float = 0f

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.CustomProgressView)
            setProgress(typedArray.getFloat(R.styleable.CustomProgressView_progress, -1f))
            val strokeColor = (typedArray.getColor(R.styleable.CustomProgressView_progressColor, 0))
            if (strokeColor != 0) setStrokeColor(strokeColor)
            startColor = (typedArray.getColor(R.styleable.CustomProgressView_startColor, 0))
            endColor = (typedArray.getColor(R.styleable.CustomProgressView_endColor, 0))
            setStrokeColor(startColor, endColor)
            setBackgroundColor(
                typedArray.getColor(
                    R.styleable.CustomProgressView_backgroundColor,
                    0
                )
            )
            strokeWidth = typedArray.getInteger(R.styleable.CustomProgressView_strokeWidth, 0)
            typedArray.recycle()
        }
    }

    override fun setBackgroundColor(color: Int) {
        borderPaint.color = color
    }

    private fun setStrokeColor(color: Int) {
        strokePaint.color = color
    }

    private fun setStrokeColor(startColor: Int, endColor: Int) {
        if (startColor != endColor) {
            strokePaint.shader =
                LinearGradient(
                    0f,
                    0f,
                    (width - 2 * BORDER_PADDING) * progress,
                    0f,
                    startColor,
                    endColor,
                    Shader.TileMode.MIRROR
                )
        }
    }

    private fun setProgress(@FloatRange(from = 0.0, to = 1.0) value: Float) {
        if (value in 0f..1f) {
            progress = value
            setStrokeColor(startColor, endColor)
            invalidate()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var width = 0
        var height = 0

        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            width = min(10 * strokeWidth, context.resources.displayMetrics.widthPixels)
        }

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            height = width / 10
        }

        strokeWidth = width / 10

        setMeasuredDimension(width, height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (w > 0 && h > 0) {
            setStrokeColor(startColor, endColor)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawLine(
            BORDER_PADDING,
            height / 2f,
            width - BORDER_PADDING,
            height / 2f,
            borderPaint
        )

        canvas?.drawLine(
            BORDER_PADDING,
            height / 2f,
            BORDER_PADDING + (width - 2 * BORDER_PADDING) * progress,
            height / 2f,
            strokePaint
        )
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    setProgress((it.x - BORDER_PADDING) / (width - 2 * BORDER_PADDING))
                }

                MotionEvent.ACTION_MOVE -> {
                    setProgress((it.x - BORDER_PADDING) / (width - 2 * BORDER_PADDING))

                }
            }
        }

        return true
    }

    private fun containsTouch(event: MotionEvent): Boolean {
        if (event.x < width - BORDER_PADDING && event.x > BORDER_PADDING && y > height / 2 - strokeWidth && y < height / 2 + strokeWidth) {
            return true
        }
        return false
    }

    companion object {
        const val BORDER_PADDING = 100f
    }
}