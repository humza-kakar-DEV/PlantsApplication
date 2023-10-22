package com.example.plantsservicefyp.blur

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.MaskFilter
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.plantsservicefyp.R
import com.example.plantsservicefyp.util.log

class BlurCustomView(context: Context, attrs: AttributeSet) :
    View(context, attrs) {

    val paint = Paint().apply {
        isAntiAlias = true
        color = resources.getColor(R.color.transparent_green)
        style = Paint.Style.FILL
        maskFilter = BlurMaskFilter(10f, BlurMaskFilter.Blur.SOLID)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(
            resolveSize(width, widthMeasureSpec),
            resolveSize(height, heightMeasureSpec)
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRoundRect(
            0f, 0f, width.toFloat()!!, height.toFloat()!!, 20f, 20f, paint
        )
    }

}