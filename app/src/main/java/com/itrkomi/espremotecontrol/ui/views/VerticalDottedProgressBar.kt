package com.itrkomi.espremotecontrol.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View


class VerticalDottedProgressBar @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null
) :
    View(context, attrs) {
    private var mHeight = 0
    private var mWidth = 0
    private var mProgress = 0
    private var mMax = 0
    @Synchronized
    fun incrementProgressBy(delta: Int) {
        setProgress(mProgress + delta)
    }

    @Synchronized
    fun setProgress(progress: Int) {
        var progress = progress
        if (progress < 0) {
            progress = 0
        }
        if (progress > mMax) {
            progress = mMax
        }
        if (progress != mProgress) {
            mProgress = progress
            refreshProgress()
        }
    }

    @Synchronized
    fun setMax(max: Int) {
        mMax = max
    }

    private fun drawSections(firstIndex: Int, lastIndex: Int, canvas: Canvas, paint: Paint) {
        for (i in firstIndex until lastIndex) {
            canvas.drawRect(
                0f,
                (2 * mSectionHeight * i).toFloat(),
                mWidth.toFloat(),
                (mSectionHeight * (2 * i + 1)).toFloat(),
                paint
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint()
        paint.color = REMAIN
        paint.style = Paint.Style.FILL
        if (mProgress == 0) {
            drawSections(0, SECTIONS_COUNT, canvas, paint)
        } else if (mProgress >= mMax) {
            paint.color = FINISHED
            drawSections(0, SECTIONS_COUNT, canvas, paint)
        } else {
            val filledCount = SECTIONS_COUNT - SECTIONS_COUNT * mProgress / mMax
            paint.color = REMAIN
            drawSections(0, filledCount, canvas, paint)
            paint.color = PROCEED
            drawSections(filledCount, SECTIONS_COUNT, canvas, paint)
        }
    }

    override fun onMeasure(widthSpecId: Int, heightSpecId: Int) {
        mHeight = MeasureSpec.getSize(heightSpecId)
        mWidth = WIDTH
        mSectionHeight = mHeight / (2 * SECTIONS_COUNT - 1)
        setMeasuredDimension(mWidth, mHeight)
    }

    @Synchronized
    private fun refreshProgress() {
        invalidate()
    }

    companion object {
        private const val WIDTH = 40
        private val REMAIN = Color.rgb(49, 49, 49)
        private val PROCEED = Color.rgb(22, 72, 237)
        private val FINISHED = Color.rgb(124, 209, 15)
        private const val SECTIONS_COUNT = 42
        private var mSectionHeight = 0
    }
}
