package com.example.clockCustomView.common.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class Clock @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val pt = Paint()
        pt.color = -0x7f000100
        pt.strokeWidth = 12.0f
        pt.style = Paint.Style.STROKE
        canvas.drawRect(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), pt)
    }
}