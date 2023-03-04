package com.example.clockCustomView.common.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.clockCustomView.common.R
import org.threeten.bp.LocalDateTime
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    companion object {
        private const val RADIUS_SCALE = 0.9f
        private const val NUMBERS_SCALE = RADIUS_SCALE - 0.1f
        private const val HOUR_ARROW_SCALE = RADIUS_SCALE - 0.5f
        private const val MINUTES_ARROW_SCALE = RADIUS_SCALE - 0.25f
        private const val SECONDS_ARROW_SCALE = RADIUS_SCALE - 0.1f

        private const val CENTER_RADIUS = 15f
        private const val DEFAULT_TEXT_SIZE = 15f

        private const val DEFAULT_STROKE_WIDTH = 5f
        private const val HOUR_ARROW_STROKE_WIDTH = DEFAULT_STROKE_WIDTH + 5f
        private const val MINUTES_ARROW_STROKE_WIDTH = DEFAULT_STROKE_WIDTH + 2f
        private const val SECONDS_ARROW_STROKE_WIDTH = DEFAULT_STROKE_WIDTH
    }

    ///////////////////////////////////////////////////////////////////////////
    // VIEW PROPERTIES
    ///////////////////////////////////////////////////////////////////////////

    private var radius = 0f
    private val rect = Rect()

    private val DEFAULT_PRIMARY_COLOR = ContextCompat.getColor(context, R.color.blue_grey)
    private val DEFAULT_SECONDARY_COLOR = Color.WHITE

    private var primaryColor = DEFAULT_PRIMARY_COLOR
    private var secondaryColor = DEFAULT_SECONDARY_COLOR

    private val clockPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = DEFAULT_STROKE_WIDTH
        color = primaryColor
    }

    private val arrowPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        color = primaryColor
    }

    ///////////////////////////////////////////////////////////////////////////
    // INIT
    ///////////////////////////////////////////////////////////////////////////

    init {
        setupAttributes(attrs)
    }

    private fun setupAttributes(attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ClockView)

        with(typedArray) {
            primaryColor = getColor(R.styleable.ClockView_primaryColor, DEFAULT_PRIMARY_COLOR)
                .also {
                    clockPaint.color = it
                    arrowPaint.color = it
                }

            secondaryColor = getColor(R.styleable.ClockView_secondaryColor, DEFAULT_SECONDARY_COLOR)
        }

        typedArray.recycle()
    }

    ///////////////////////////////////////////////////////////////////////////
    // DRAWING
    ///////////////////////////////////////////////////////////////////////////

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        radius = min(measuredHeight, measuredWidth) / 2f

        canvas.apply {
            setBackgroundColor(secondaryColor)
            drawClockShape()
            drawCenter()
            drawNumbers()
            drawArrows()
        }

        postInvalidateDelayed(1000)
    }

    private fun Canvas.drawClockShape() {
        clockPaint.style = Paint.Style.STROKE
        drawCircle(
            radius,
            radius,
            radius * RADIUS_SCALE,
            clockPaint
        )
    }

    private fun Canvas.drawCenter() {
        clockPaint.style = Paint.Style.FILL
        drawCircle(
            radius,
            radius,
            CENTER_RADIUS,
            clockPaint
        )
    }

    private fun Canvas.drawNumbers() {
        clockPaint.textSize = radius / 100 * DEFAULT_TEXT_SIZE

        for (i in 1..12) {
            val title = i.toString()

            clockPaint.getTextBounds(title, 0, title.length, rect)


            val angle = Math.PI / 6 * (i - 3)
            val x = radius - rect.width() / 2 + NUMBERS_SCALE * cos(angle) * radius
            val y = radius + rect.height() / 2 + NUMBERS_SCALE * sin(angle) * radius

            drawText(title, x.toFloat(), y.toFloat(), clockPaint)
        }
    }

    private fun Canvas.drawArrow(value: Double, lengthScale: Float, strokeWidth: Float) {
        val angle = Math.PI * value / 30 - Math.PI / 2
        arrowPaint.strokeWidth = strokeWidth

        drawLine(
            radius,
            radius,
            radius + cos(angle).toFloat() * radius * lengthScale,
            radius + sin(angle).toFloat() * radius * lengthScale,
            arrowPaint
        )
    }

    private fun Canvas.drawArrows() = with(LocalDateTime.now()) {
        drawArrow(
            value = (hour % 12 + minute / 60f) * 5.0,
            lengthScale = HOUR_ARROW_SCALE,
            strokeWidth = HOUR_ARROW_STROKE_WIDTH
        )

        drawArrow(
            value = minute.toDouble(),
            lengthScale = MINUTES_ARROW_SCALE,
            strokeWidth = MINUTES_ARROW_STROKE_WIDTH
        )

        drawArrow(
            value = second.toDouble(),
            lengthScale = SECONDS_ARROW_SCALE,
            strokeWidth = SECONDS_ARROW_STROKE_WIDTH
        )
    }
}